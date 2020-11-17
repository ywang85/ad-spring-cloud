package com.imooc.ad.mysql.listener;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import com.imooc.ad.mysql.TemplateHolder;
import com.imooc.ad.dto.BinlogRowData;
import com.imooc.ad.dto.TableTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AggregationListener implements BinaryLogClient.EventListener {

    private String dbName;
    private String tableName;

    // 表名 -> 监听处理方法
    private Map<String, Ilistener> listenerMap = new HashMap<>();

    @Autowired
    private TemplateHolder templateHolder;

    private String genKey(String dbName, String tableName) {
        return dbName + ":" + tableName;
    }

    public void register(String _dbName, String _tableName, Ilistener ilistener) {
        log.info("register : {}-{}", _dbName, _tableName);
        listenerMap.put(genKey(_dbName, _tableName), ilistener);
    }

    @Override
    public void onEvent(Event event) {
        EventType type = event.getHeader().getEventType();
        log.debug("event type: {}", type);
        if (type == EventType.TABLE_MAP) {
            TableMapEventData data = event.getData();
            tableName = data.getTable();
            dbName = data.getDatabase();
            return;
        }

        if (type != EventType.EXT_UPDATE_ROWS && type != EventType.EXT_WRITE_ROWS && type != EventType.EXT_DELETE_ROWS) {
            return;
        }

        // 表名库名是否已经完成填充
        if (StringUtils.isEmpty(dbName) || StringUtils.isEmpty(tableName)) {
            log.error("no meta data event");
            return;
        }
        // 找出对应表有兴趣的监听器
        String key = genKey(dbName, tableName);
        Ilistener ilistener = listenerMap.get(key);
        if (ilistener == null) {
            log.debug("skip {}", key);
            return;
        }
        log.info("trigger event: {}", type.name());

        try {
            BinlogRowData rowData = buildRowData(event.getData());
            if (rowData == null) {
                return;
            }
            rowData.setEventType(type);
            ilistener.onEvent(rowData);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            dbName = "";
            tableName = "";
        }
    }

    private List<Serializable[]> getAfterValues(EventData eventData) {
        if (eventData instanceof WriteRowsEventData) {
            return ((WriteRowsEventData) eventData).getRows();
        }
        if (eventData instanceof UpdateRowsEventData) {
            return ((UpdateRowsEventData) eventData).getRows().stream().map(Map.Entry::getValue).collect(Collectors.toList());
        }
        if (eventData instanceof DeleteRowsEventData) {
            return ((DeleteRowsEventData) eventData).getRows();
        }
        return Collections.emptyList();
    }

    private BinlogRowData buildRowData(EventData eventData) {
        TableTemplate tableTemplate = templateHolder.getTable(tableName);
        if (tableTemplate == null) {
            log.warn("table {} not found", tableName);
        }

        List<Map<String, String>> afterMapList = new ArrayList<>();
        for (Serializable[] afterValue : getAfterValues(eventData)) {
            Map<String, String> afterMap = new HashMap<>();
            // 记录每一个操作有几行
            int colLen = afterValue.length;
            for (int i = 0; i < colLen; i++) {
                // 取出当前的位置对应的列名
                String colName = tableTemplate.getPosMap().get(i);
                // 如果没有则说明不关心这个列
                if (colName == null) {
                    log.debug("ignore position: {}", i);
                    continue;
                }
                // 修改的具体操作
                String colValue = afterValue[i].toString();
                afterMap.put(colName, colValue);
            }
            afterMapList.add(afterMap);
        }
        BinlogRowData rowData = new BinlogRowData();
        rowData.setAfter(afterMapList);
        rowData.setTable(tableTemplate);
        return rowData;
    }
}

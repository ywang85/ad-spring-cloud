package com.imooc.ad.dto;

import com.github.shyiko.mysql.binlog.event.EventType;
import lombok.Data;

import java.util.List;
import java.util.Map;

// binlog Event -> BinlogRowData
@Data
public class BinlogRowData {
    private TableTemplate table;
    private EventType eventType;
    // key: 列名，value：列值
    private List<Map<String, String>> after;
    private List<Map<String, String>> before;
}

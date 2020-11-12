package com.imooc.ad.mysql;

import com.alibaba.fastjson.JSON;
import com.imooc.ad.mysql.constant.OpType;
import com.imooc.ad.mysql.dto.ParseTemplate;
import com.imooc.ad.mysql.dto.TableTemplate;
import com.imooc.ad.mysql.dto.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class TemplateHolder {
    // 记录了对数据库所有表的所有操作
    private ParseTemplate template;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String SQL_SCHEMA = "select table_schema, table_name, column_name, ordinal_position from " +
            "information_schema.columns where table_schema = ? and table_name = ?";

    private void loadJson(String path) {
        // 获取resource下面的文件
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = cl.getResourceAsStream(path);

        try {
            // 将template.json解析成Template对象
            Template template = JSON.parseObject(inputStream, Charset.defaultCharset(), Template.class);
            // 将Template对象中的每个表每个层级支持的操作都保存在ParseTemplate的tableTemplateMap里面，key是表名
            this.template = ParseTemplate.parse(template);
            // 对每张表的修改字段和ORDINAL_POSITION做映射，为了和binlog数据匹配需要-1
            loadMeta();
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException("fail to parse json file");
        }
    }

    @PostConstruct
    private void init() {
        loadJson("template.json");
    }

    public TableTemplate getTable(String tableName) {
        return template.getTableTemplateMap().get(tableName);
    }

    // 对每张表的修改字段和ORDINAL_POSITION做映射，为了和binlog数据匹配需要-1
    private void loadMeta() {
        for (Map.Entry<String, TableTemplate> entry: template.getTableTemplateMap().entrySet()) {
            TableTemplate tableTemplate = entry.getValue();
            List<String> updateFields = tableTemplate.getOpTypeFieldSetMap().get(OpType.UPDATE);
            List<String> deleteFields = tableTemplate.getOpTypeFieldSetMap().get(OpType.DELETE);
            List<String> insertFields = tableTemplate.getOpTypeFieldSetMap().get(OpType.ADD);

            jdbcTemplate.query(SQL_SCHEMA, new Object[]{
                    template.getDatabase(), tableTemplate.getTableName()
            }, (rs, i) -> {
                int pos = rs.getInt("ORDINAL_POSITION");
                String colName = rs.getString("COLUMN_NAME");
                if ((null != updateFields && updateFields.contains(colName)) ||
                        (null != insertFields && insertFields.contains(colName) ||
                                null != deleteFields && deleteFields.contains(colName))) {
                    tableTemplate.getPosMap().put(pos - 1, colName);
                }
                return null;
            });
        }
    }
}

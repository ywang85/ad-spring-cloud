package com.imooc.ad.dto;

import com.imooc.ad.constant.OpType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
// 操作读取表信息
public class TableTemplate {
    private String tableName;
    private String level;

    private Map<OpType, List<String>> opTypeFieldSetMap = new HashMap<>();
    // 字段索引 -> 字段名，因为binlog不会打印列名，只有索引，所以需要映射
    private Map<Integer, String> posMap = new HashMap<>();
}

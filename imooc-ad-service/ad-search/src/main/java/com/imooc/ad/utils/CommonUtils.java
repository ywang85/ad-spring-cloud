package com.imooc.ad.utils;

import java.util.Map;
import java.util.function.Supplier;

public class CommonUtils {
    // 如果key不存在，从factory返回一个新的对象V
    public static <K, V> V getOrCreate(K key, Map<K, V> map, Supplier<V> factory) {
        return map.computeIfAbsent(key, k -> factory.get());
        // 等价于
//        V v = map.get(key);
//        if (v == null) {
//            v = (V) new Object();
//            map.put(key, v);
//        }
    }

    public static String stringConcat(String... args) {
        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
            sb.append(arg);
            sb.append("-");
        }
        return sb.deleteCharAt(sb.length()).toString();
    }
}

package com.imooc.ad.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

@Slf4j
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
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    // Tue Jan 01 09:00:00 CST 2019
    public static Date parseStringDate(String dateString) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
            return DateUtils.addHours(dateFormat.parse(dateString), -8);
        } catch (ParseException e) {
            log.error("parseStringDate error: {}", dateString);
            return null;
        }
    }
}

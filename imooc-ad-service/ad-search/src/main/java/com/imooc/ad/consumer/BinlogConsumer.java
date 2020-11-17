package com.imooc.ad.consumer;

import com.alibaba.fastjson.JSON;
import com.imooc.ad.dto.MySqlRowData;
import com.imooc.ad.sender.ISender;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 消费binlog message
 */
@Slf4j
@Component
public class BinlogConsumer {
    @Autowired
    private ISender sender;

    @KafkaListener(topics = {"ad-search-mysql-data"}, groupId = "ad-search")
    public void processMysqlRowData(ConsumerRecord<?, ?> record) {
        Optional<?> kafakMessage = Optional.ofNullable(record.value());
        if (kafakMessage.isPresent()) {
            Object message = kafakMessage.get();
            MySqlRowData rowData = JSON.parseObject(message.toString(), MySqlRowData.class);
            log.info("kafka process MysqlRowData: {}", JSON.toJSONString(rowData));
            sender.sender(rowData);
        }
    }
}

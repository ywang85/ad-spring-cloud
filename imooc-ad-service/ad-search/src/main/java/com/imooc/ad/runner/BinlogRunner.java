package com.imooc.ad.runner;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BinlogRunner implements CommandLineRunner {
    @Autowired
    private BinaryLogClient client;

    @Override
    public void run(String... args) throws Exception {
        log.info("Coming in Binlog Runner");
        client.connect();
    }
}

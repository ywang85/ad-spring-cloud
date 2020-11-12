package com.imooc.ad.service;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;

import java.io.IOException;

public class BinlogServiceTest {
    public static void main(String[] args) throws IOException {
        BinaryLogClient client = new BinaryLogClient("127.0.0.1", 3306, "root", "wyj931109");
        client.registerEventListener(new BinaryLogClient.EventListener() {
            @Override
            public void onEvent(Event event) {
                EventData data = event.getData();
                if (data instanceof UpdateRowsEventData) {
                    // UpdateRowsEventData{tableId=100, includedColumnsBeforeUpdate={0, 1, 2}, includedColumns={0, 1, 2}, rows=[
                    //    {before=[10, 10, 标志], after=[10, 10, 奔驰]}
                    //]}
                    System.out.println("Update");
                    System.out.println(data.toString());
                } else if (data instanceof WriteRowsEventData) {
                    // WriteRowsEventData{tableId=100, includedColumns={0, 1, 2}, rows=[
                    //    [10, 10, 标志]
                    //]}
                    System.out.println("Write");
                    System.out.println(data.toString());
                } else if (data instanceof DeleteRowsEventData) {
                    // DeleteRowsEventData{tableId=100, includedColumns={0, 1, 2}, rows=[
                    //    [10, 10, 标志]
                    //]}
                    System.out.println("Delete");
                    System.out.println(data.toString());
                }
            }
        });
        client.connect();
    }
}

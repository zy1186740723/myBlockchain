package com.mindata.blockchain.core.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;

/**
 * @Author: zhangyan
 * @Date: 2019/4/15 23:11
 * @Version 1.0
 */
@Service
public class TestUtilsService {


    private static ArrayList<String> timeResList = new ArrayList<>();

    public static ArrayList<String> timeResCollector(String time) {
        timeResList.add(time + "s,");
        return timeResList;

    }

    private static FileOutputStream outputStream;

    static {
        try {
            //如果文件存储在，就在文件末尾追加写入
            outputStream = new FileOutputStream("demo1",true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void testDemo1(ArrayList<String> timeList) throws IOException {

        for (String time : timeList) {
            byte[] res = time.getBytes("utf-8");
            outputStream.write(res);
        }
        outputStream.close();
    }

    public static void recordAddBlockTime(String time) throws IOException {


        try {
            byte[] res = time.getBytes("utf-8");
            outputStream.write(res);
        } catch (IOException e) {
            outputStream.close();
            e.printStackTrace();
        }
    }

}


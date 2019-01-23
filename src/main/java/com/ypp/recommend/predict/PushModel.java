package com.ypp.recommend.predict;

import redis.clients.jedis.Jedis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PushModel {
  public static void main(String[] args) {
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    String today = dateFormat.format(cal.getTime());
    String relativePath = "/home/data/testXgboost/";
    String modelPath = "model/"+today+".model";
    Jedis jedis = new Jedis("r-bp105a3f000fa7f4313.re  dis.rds.aliyuncs.com");
    // Jedis jedis = new Jedis("c855afad0a1e47ec829.redis.rds.aliyuncs.com");
    jedis.auth("qaIEUZ2GXxzLzAfo");
    // jedis.auth("WYWKypp2016test");
    File file = new File(relativePath+modelPath);
    Long filelength = file.length();
    byte[] filecontent = new byte[filelength.intValue()];
    FileInputStream in = null;
    try {
      in = new FileInputStream(file);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    try {
      in.read(filecontent);
    } catch (IOException e) {
      e.printStackTrace();
    }
    byte[] modelPathBytes = modelPath.getBytes();
    jedis.set(modelPathBytes,filecontent);
    jedis.expire(modelPathBytes,48*60*60);
  }
}

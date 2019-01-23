package com.ypp.recommend.predict;

import ml.dmlc.xgboost4j.java.Booster;
import ml.dmlc.xgboost4j.java.DMatrix;
import ml.dmlc.xgboost4j.java.XGBoost;
import ml.dmlc.xgboost4j.java.XGBoostError;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class SaveModel {

    public static void saveDumpModel(String modelPath, String[] modelInfos) throws IOException {
        try {
            PrintWriter writer = new PrintWriter(modelPath, "UTF-8");
            for (int i = 0; i < modelInfos.length; ++i) {
                writer.print("booster[" + i + "]:\n");
                writer.print(modelInfos[i]);
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void trainAndtest() throws XGBoostError, FileNotFoundException {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String yesterday = dateFormat.format(cal.getTime());
        String trainPath = "/home/data/testXgboost/model/train_" + yesterday + ".txt";
        DMatrix trainMat = new DMatrix(trainPath);
        DMatrix testMat = new DMatrix("/home/data/testXgboost/model/test.txt");

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("eta", 0.01);
        params.put("max_depth", 7);
        params.put("silent", 1);
        params.put("gamma", 0.2);
        params.put("eval_metric", "auc");
        params.put("objective", "binary:logistic");

        HashMap<String, DMatrix> watches = new HashMap<String, DMatrix>();
        watches.put("train", trainMat);
        watches.put("test", testMat);

        //set round
        int round = 1200;

        //train a boost model
        Booster booster = XGBoost.train(trainMat, params, round, watches, null, null);

        String today = dateFormat.format(new Date());
        String modelPath = "/home/data/testXgboost/model/" + today + ".model";
        booster.saveModel(modelPath);

        //将model传入redis 过期时间48h
        // byte[] modelKey = modelPath.getBytes();
        // File file = new File(modelPath);
        // Long filelength = file.length();
        // byte[] filecontent = new byte[filelength.intValue()];
        // FileInputStream in = new FileInputStream(file);
        // Jedis jedis = new Jedis("r-bp105a3f000fa7f4313.redis.rds.aliyuncs.com");
        // jedis.auth("qaIEUZ2GXxzLzAfo");
        // try {
        //     in.read(filecontent);
        //     jedis.set(modelKey, filecontent);
        //     jedis.expire(modelKey, 48 * 60 * 60);
        // } catch (IOException e) {
        //     e.printStackTrace();
        // } finally {
        //     jedis.close();
        //     System.exit(0);
        // }


//        String[] modelInfos = booster.getModelDump("model/featmap.txt", false);
//        saveDumpModel("model/dump.raw.txt", modelInfos);
//        Map<String, Integer> featureScore = booster.getFeatureScore(modelInfos);
//        System.out.println(featureScore);
    }

}

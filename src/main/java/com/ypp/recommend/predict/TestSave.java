package com.ypp.recommend.predict;

import ml.dmlc.xgboost4j.java.XGBoostError;

import java.io.IOException;

public class TestSave {
    public static void main(String[] args) throws IOException, XGBoostError {
        SaveModel.trainAndtest();
    }
}

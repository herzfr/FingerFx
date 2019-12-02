package com.travest.fingerfx.utility;

import com.travest.fingerfx.Entity.Record;

public class AppData {

    public static Record record;
    public static String token;

    public AppData() {
    }

    public static Record getRecord() {
        return record;
    }

    public static void setRecord(Record record) {
        AppData.record = record;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        AppData.token = token;
    }
}

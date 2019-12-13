package com.travest.fingerfx.utility;

import com.travest.fingerfx.Entity.Finger;
import com.travest.fingerfx.Entity.Record;

import java.util.List;

public class AuthenticateData {

    public static List<Finger> fingerList;

    public static List<Finger> getFingerList() {
        return fingerList;
    }

    public static void setFingerList(List<Finger> fingerList) {
        AuthenticateData.fingerList = fingerList;
    }
}

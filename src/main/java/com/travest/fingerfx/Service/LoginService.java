package com.travest.fingerfx.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travest.fingerfx.Entity.AuthenticateErrorResult;
import com.travest.fingerfx.Entity.LoginResult;
import com.travest.fingerfx.Entity.Record;
import com.travest.fingerfx.utility.AppData;
import com.travest.fingerfx.utility.Consts;
import com.travest.fingerfx.utility.Dialog;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class LoginService {

    private final OkHttpClient client = new OkHttpClient();

    private Record record;
    private String token;
    private String message;

    public AppData appData;
    ServerRequest serverRequest = new ServerRequest();

    public Boolean loginRequest(String username, String password) throws IOException {

        OkHttpClient client1 = client.newBuilder()
                .readTimeout(Consts.readTimeout, TimeUnit.MILLISECONDS)
                .connectTimeout(Consts.readTimeout, TimeUnit.MILLISECONDS)
                .build();

        RequestBody requestBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();

        Request request = new Request.Builder()
                .url(Consts.host + "authenticate")
                .post(requestBody)
                .build();

        try {
            Response response = client1.newCall(request).execute();
            if (response.code() == 200) {
//                String jsonString = response.body().string();
                LoginResult result = new ObjectMapper().readValue(response.body().string(), LoginResult.class);
                record = result.getRecord();
                token = result.getToken();

                appData.setRecord(record);
                appData.setToken(token);

                try {
                    serverRequest.getFinger(record.getUsername(), token);
                } catch (Exception e) {

                }


                return true;

            } else {
//                String jsonString = response.body().string();
                AuthenticateErrorResult result = new ObjectMapper().readValue(response.body().string(), AuthenticateErrorResult.class);
                message = result.getMessage();
                Dialog.errorMessage("Login Error", message);
                return false;
            }
        } catch (Exception e) {
//            System.out.println(e.getMessage());
            Dialog.errorMessage("Login Connection Error", e.getMessage());
            return false;

        }

    }

}

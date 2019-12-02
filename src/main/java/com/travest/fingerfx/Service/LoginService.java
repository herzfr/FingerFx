package com.travest.fingerfx.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travest.fingerfx.Entity.AuthenticateErrorResult;
import com.travest.fingerfx.Entity.LoginResult;
import com.travest.fingerfx.Entity.Record;
import com.travest.fingerfx.MainApp;
import com.travest.fingerfx.utility.Consts;
import com.travest.fingerfx.utility.Dialog;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

public class LoginService {

    private final OkHttpClient client = new OkHttpClient();

    @FXML
    private AnchorPane loginAnchor;

    private Record record;
    private String token;
    private String message;

    public Boolean status;
    private Handler mHandler;

    public Boolean loginRequest(String username, String password) throws IOException {

//        OkHttpClient client = new OkHttpClient();
        OkHttpClient client1 = client.newBuilder()
                .readTimeout(500, TimeUnit.MILLISECONDS)
                .connectTimeout(2000, TimeUnit.MILLISECONDS)
                .build();

        RequestBody requestBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();

        Request request = new Request.Builder()
                .url(Consts.host + "authenticate")
                .post(requestBody)
                .build();

        try{
            Response response = client1.newCall(request).execute();
            if (response.code() == 200) {
                String jsonString = response.body().string();
                LoginResult result = new ObjectMapper().readValue(jsonString, LoginResult.class);
                record = result.getRecord();
                token = result.getToken();
                MainApp app = new MainApp();
                app.setRecord(record);
                System.out.println("record :" + record.getUsername());
                return true;

            } else {
                String jsonString = response.body().string();
                AuthenticateErrorResult result = new ObjectMapper().readValue(jsonString, AuthenticateErrorResult.class);
                message = result.getMessage();
                Dialog.errorMessage("Login Error", message);
                return false;
            }
        }catch (Exception e )
        {
            System.out.println(e.getMessage());
            Dialog.errorMessage("Login Connection Error", e.getMessage());
            return false;

        }



    }

}

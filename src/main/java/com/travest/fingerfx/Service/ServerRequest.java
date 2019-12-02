package com.travest.fingerfx.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travest.fingerfx.Entity.AuthenticateErrorResult;
import com.travest.fingerfx.Entity.LoginResult;
import com.travest.fingerfx.Entity.Record;
import com.travest.fingerfx.utility.Consts;
import com.travest.fingerfx.utility.Dialog;
import javafx.application.Platform;
import okhttp3.*;

import java.io.IOException;

public class ServerRequest {

    private Record record;
    private String token;

    public void loginRequest(String userName , String userPassword){
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new FormBody.Builder()
                .add("username", userName)
                .add("password", userPassword)
                .build();

        Request request = new Request.Builder()
                .url(Consts.host + "authenticate")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> {
                    Dialog.errorMessage("Connect Error", "Failed to connect to server ");
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    int code = response.code();
                    String jsonString = response.body().string();

                    if (code == 200) {
                        LoginResult result = new ObjectMapper().readValue(jsonString, LoginResult.class);
                        record = result.getRecord();
                        token = result.getToken();
                        System.out.println(code);
                        System.out.println(result.getRecord().getUsername());
                        Platform.runLater(
                                () -> {
//                                    try {
////                                        homeScene();
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
                                }
                        );

                    } else {
                        Platform.runLater(() -> {
                            try {
                                AuthenticateErrorResult result = new ObjectMapper().readValue(jsonString, AuthenticateErrorResult.class);
                                String message = result.getMessage();
                                Dialog.errorMessage("Login Errror", message);
                                System.out.println(code);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    call.cancel();
                } finally {
                    response.body().close();
                    call.cancel();
                }
            }
        });
    }

}

package com.travest.fingerfx.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travest.fingerfx.Entity.AuthenticateErrorResult;
import com.travest.fingerfx.Entity.LoginResult;
import com.travest.fingerfx.Entity.Record;
import javafx.application.Platform;
import okhttp3.*;

import java.io.IOException;

public class LoginService {

    private Record record;
    private String token;
    private String message;

    public void loginRequest(String username, String password) {


        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
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
                    String jsonString = response.body().string();
                    int code = response.code();

                    if (code == 200) {
                        LoginResult result = new ObjectMapper().readValue(jsonString, LoginResult.class);
                        record = result.getRecord();
                        token = result.getToken();
                        boolean success = result.getSuccess();
                        System.out.println("Success => " + success);

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
                        Platform.runLater(
                                () -> {
                                    try {
                                        AuthenticateErrorResult result = new ObjectMapper().readValue(jsonString, AuthenticateErrorResult.class);
                                        message = result.getMessage();
                                        Dialog.errorMessage("Login Error", message);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                        );
                    }







                } catch (IOException e) {
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

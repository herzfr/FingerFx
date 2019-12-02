package com.travest.fingerfx.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travest.fingerfx.Entity.AuthenticateErrorResult;
import com.travest.fingerfx.Entity.LoginResult;
import com.travest.fingerfx.Entity.Record;
import com.travest.fingerfx.MainApp;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import okhttp3.*;

import java.io.IOException;
import java.util.logging.Handler;

public class LoginService {


    @FXML
    private AnchorPane loginAnchor;

    private Record record;
    private String token;
    private String message;

    public Boolean status;
    private Handler mHandler;

    public Boolean loginRequest(String username, String password) throws IOException {

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();

        Request request = new Request.Builder()
                .url(Consts.host + "authenticate")
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
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
        } else {

            Dialog.errorMessage("Login Error", "Failed to connect server");
            return false;
        }


//        client.newCall(request).enqueue(new Callback() {
//
//            @Override
//            public void onFailure(Call call, IOException e) {
//                status = false;
//                Platform.runLater(() -> {
//                    status = false;
//                    Dialog.errorMessage("Connect Error", "Failed to connect to server ");
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                try {
//                    String jsonString = response.body().string();
//                    int code = response.code();
//                    status = true;
//                    if (code == 200) {
//                        LoginResult result = new ObjectMapper().readValue(jsonString, LoginResult.class);
//                        record = result.getRecord();
//                        token = result.getToken();
//                        status = true;
//                        Platform.runLater(
//                                () -> {
//                                    MainApp app = new MainApp();
//                                    app.setRecord(record);
//                                    System.out.println("record :" + record.getUsername());
//                                    status = true;
//                                }
//                        );
//                    } else {
//                        Platform.runLater(
//                                () -> {
//                                    try {
//                                        AuthenticateErrorResult result = new ObjectMapper().readValue(jsonString, AuthenticateErrorResult.class);
//                                        message = result.getMessage();
//                                        Dialog.errorMessage("Login Error", message);
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                        );
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    call.cancel();
//                } finally {
//                    response.body().close();
//                    call.cancel();
//                }
//            }
//        });
//        return status;
    }

}

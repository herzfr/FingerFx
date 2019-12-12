package com.travest.fingerfx.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travest.fingerfx.Entity.AuthenticateErrorResult;
import com.travest.fingerfx.Entity.Finger;
import com.travest.fingerfx.Entity.RequestResult;
import com.travest.fingerfx.Entity.RequestResult2;
import com.travest.fingerfx.utility.AppData;
import com.travest.fingerfx.utility.Consts;
import com.travest.fingerfx.utility.Dialog;
import javafx.scene.control.Alert;
import okhttp3.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

public class ServerRequest {

    private final OkHttpClient client = new OkHttpClient();

    private MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private Finger data;
    private String token;
    private String message;

    public Boolean status;

    public AppData appData;

    public Boolean registerNewFinger(String username, byte[] template, String token, BufferedImage finger) throws IOException {

        String encodedTemplate = Base64.getEncoder().encodeToString(template);
//        String decoded = new String(Base64.getDecoder().decode(encodedTemplate.getBytes()));

        String image = null;
        ByteArrayOutputStream bAOS = new ByteArrayOutputStream();

        try {
            ImageIO.write(finger, "png", bAOS);
            byte[] imageByte = bAOS.toByteArray();
            image = Base64.getEncoder().encodeToString(imageByte);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String jsonInString = "{\"username\":\""+username+"\",\"finger\":\"" + image + "\",\"template\":\"" + encodedTemplate + "\"}";

//        System.out.println("token : " + appData.getToken());

        OkHttpClient client1 = client.newBuilder()
                .readTimeout(Consts.readTimeout, TimeUnit.MILLISECONDS)
                .connectTimeout(Consts.readTimeout, TimeUnit.MILLISECONDS)
                .build();

        RequestBody body = RequestBody.create(JSON, jsonInString);

        Request request = new Request.Builder()
                .url(Consts.host + "editFinger")
                .header("Authorization", "Bearer " + token)
                .put(body)
                .build();
        try {
            Response response = client1.newCall(request).execute();
            if (response.code() == 200) {
//                String jsonString = response.body().string();
                RequestResult result = new ObjectMapper().readValue(response.body().string(), RequestResult.class);

                System.out.println(result.getMessage());


                response.close();
                return true;
            } else {
//                String jsonString = response.body().string();
                AuthenticateErrorResult result = new ObjectMapper().readValue(response.body().string(), AuthenticateErrorResult.class);
                message = result.getMessage();
                Dialog.errorMessage("Request Error", message);
                response.close();
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Dialog.errorMessage("Request Connection Error", e.getMessage());
            return false;

        }


    }


    public Boolean getFinger(String username, String token) {

        OkHttpClient client1 = client.newBuilder()
                .readTimeout(Consts.readTimeout, TimeUnit.MILLISECONDS)
                .connectTimeout(Consts.readTimeout, TimeUnit.MILLISECONDS)
                .build();

        RequestBody requestBody = new FormBody.Builder()
                .add("username", username)
                .build();

        Request request = new Request.Builder()
                .url(Consts.host + "finger")
                .header("Authorization", "Bearer " + token)
                .post(requestBody)
                .build();

        try {
            Response response = client1.newCall(request).execute();
            if (response.code() == 200) {
                RequestResult2 result = new ObjectMapper().readValue(response.body().string(), RequestResult2.class);

                AppData.setFinger(result.getRecord());

                response.close();
                return true;
            } else {
                AuthenticateErrorResult result = new ObjectMapper().readValue(response.body().string(), AuthenticateErrorResult.class);
                message = result.getMessage();
                Dialog.errorMessage("Request Error", message);
                response.close();
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Dialog.errorMessage("Request Connection Error", e.getMessage());
            return false;

        }


    }


}

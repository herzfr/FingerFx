package com.travest.fingerfx.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travest.fingerfx.Entity.*;
import com.travest.fingerfx.utility.AppData;
import com.travest.fingerfx.utility.AuthenticateData;
import com.travest.fingerfx.utility.Consts;
import com.travest.fingerfx.utility.Dialog;
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

    AppData appData;

    public Boolean updateFinger(String username, byte[] template, byte[] template2, BufferedImage finger, BufferedImage finger2, String token) throws IOException {

        String encodedTemplate = Base64.getEncoder().encodeToString(template);
        String encodedTemplate2 = Base64.getEncoder().encodeToString(template2);

        String fingerImage1 = null;
        String fingerImage2 = null;
        ByteArrayOutputStream bAOS = new ByteArrayOutputStream();
        ByteArrayOutputStream bAOS2 = new ByteArrayOutputStream();

        try {
            ImageIO.write(finger, "gif", bAOS);
            ImageIO.write(finger2, "gif", bAOS);
            byte[] imageByte = bAOS.toByteArray();
            byte[] imageByte2 = bAOS2.toByteArray();
            fingerImage1 = Base64.getEncoder().encodeToString(imageByte);
            fingerImage2 = Base64.getEncoder().encodeToString(imageByte2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String jsonString = "{\n" +
                "    \"username\": \"" + username + "\",\n" +
                "    \"finger\": \"" + fingerImage1 + "\",\n" +
                "    \"finger2\": \"" + fingerImage2 + "\",\n" +
                "    \"template\": \"" + encodedTemplate + "\",\n" +
                "    \"template2\": \"" + encodedTemplate2 + "\"\n" +
                "  }";

        OkHttpClient client1 = client.newBuilder()
                .readTimeout(Consts.readTimeout, TimeUnit.MILLISECONDS)
                .connectTimeout(Consts.readTimeout, TimeUnit.MILLISECONDS)
                .build();

        RequestBody body = RequestBody.create(JSON, jsonString);

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

    public Boolean registerFinger(String username, byte[] template, byte[] template2, BufferedImage finger, BufferedImage finger2, String token) throws IOException {
        String encodedTemplate = Base64.getEncoder().encodeToString(template);
        String encodedTemplate2 = Base64.getEncoder().encodeToString(template2);

        String fingerImage1 = null;
        String fingerImage2 = null;
        ByteArrayOutputStream bAOS = new ByteArrayOutputStream();
        ByteArrayOutputStream bAOS2 = new ByteArrayOutputStream();

        try {
            ImageIO.write(finger, "gif", bAOS);
            byte[] imageByte = bAOS.toByteArray();
            fingerImage1 = Base64.getEncoder().encodeToString(imageByte);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ImageIO.write(finger2, "gif", bAOS2);
            byte[] imageByte2 = bAOS2.toByteArray();
            fingerImage2 = Base64.getEncoder().encodeToString(imageByte2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String jsonString = "{\n" +
                "    \"username\": \"" + username + "\",\n" +
                "    \"finger\": \"" + fingerImage1 + "\",\n" +
                "    \"finger2\": \"" + fingerImage2 + "\",\n" +
                "    \"template\": \"" + encodedTemplate + "\",\n" +
                "    \"template2\": \"" + encodedTemplate2 + "\"\n" +
                "  }";

        OkHttpClient client1 = client.newBuilder()
                .readTimeout(Consts.readTimeout, TimeUnit.MILLISECONDS)
                .connectTimeout(Consts.readTimeout, TimeUnit.MILLISECONDS)
                .build();

        RequestBody body = RequestBody.create(JSON, jsonString);

        Request request = new Request.Builder()
                .url(Consts.host + "addFinger")
                .header("Authorization", "Bearer " + token)
                .post(body)
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

                appData.setFinger(result.getRecord());
                appData.setIsFingerRegistered(true);

                response.close();
                return true;
            } else {
                System.out.println("error");
                Dialog.errorMessage("Request Error", "Error Get Finger/Finger Not Registered");
                response.close();
                appData.setIsFingerRegistered(false);
                return false;
            }
        } catch (Exception e) {
            System.out.println("pesan"+e.getMessage());
            Dialog.errorMessage("Request Connection Error", e.getMessage());
            appData.setIsFingerRegistered(false);
            return false;

        }


    }

  public Boolean getAuthFinger() {

        OkHttpClient client1 = client.newBuilder()
                .readTimeout(Consts.readTimeout, TimeUnit.MILLISECONDS)
                .connectTimeout(Consts.readTimeout, TimeUnit.MILLISECONDS)
                .build();

//        RequestBody requestBody = new FormBody.Builder()
//                .add("username", username)
//                .build();

        Request request = new Request.Builder()
                .url(Consts.host + "getFinger")
                .build();

        try {
            Response response = client1.newCall(request).execute();
            if (response.code() == 200) {
                ListFinger result = new ObjectMapper().readValue(response.body().string(), ListFinger.class);

//                System.out.println("template index 0 : "+result.getFinger().get(0).getTemplate());

                AuthenticateData.setFingerList(result.getFinger());

                response.close();
                return true;
            } else {
                System.out.println("error");
                Dialog.errorMessage("Request Error", "Error Get Finger Data");
                response.close();
                appData.setIsFingerRegistered(false);
                return false;
            }
        } catch (Exception e) {
            System.out.println("pesan"+e.getMessage());
            Dialog.errorMessage("Request Connection Error", e.getMessage());
            appData.setIsFingerRegistered(false);
            return false;

        }


    }


}

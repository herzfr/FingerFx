package com.travest.fingerfx.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travest.fingerfx.Entity.AuthenticateErrorResult;
import com.travest.fingerfx.Entity.Finger;
import com.travest.fingerfx.Entity.RequestResult;
import com.travest.fingerfx.utility.AppData;
import com.travest.fingerfx.utility.Consts;
import com.travest.fingerfx.utility.Dialog;
import okhttp3.*;

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

    public Boolean registerNewFinger(String username, byte[] template, String token) throws IOException {
        String encoded = Base64.getEncoder().encodeToString(template);
        String decoded = new String(Base64.getDecoder().decode(encoded.getBytes()));

        System.out.println("original : " + new String(template));
        System.out.println("encode decode  : " + decoded);

        String jsonInString = "{\"username\":\"11\",\"finger\":\"" + "fingernya " + "\",\"template\":\"" + encoded + "\"}";


        System.out.println("token : " + appData.getToken());

        OkHttpClient client1 = client.newBuilder()
                .readTimeout(Consts.readTimeout, TimeUnit.MILLISECONDS)
                .connectTimeout(Consts.readTimeout, TimeUnit.MILLISECONDS)
                .build();

        RequestBody body = RequestBody.create(JSON, jsonInString);

        Request request = new Request.Builder()
                .url(Consts.host + "finger/add")
                .header("Authorization", "Bearer " + token)
                .post(body)
                .build();

        try {
            Response response = client1.newCall(request).execute();
            if (response.code() == 200) {
//                String jsonString = response.body().string();
                RequestResult result = new ObjectMapper().readValue(response.body().string(), RequestResult.class);

                System.out.println(result.getMessage());


                return true;

            } else {
//                String jsonString = response.body().string();
                AuthenticateErrorResult result = new ObjectMapper().readValue(response.body().string(), AuthenticateErrorResult.class);
                message = result.getMessage();
                Dialog.errorMessage("Request Error", message);
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Dialog.errorMessage("Request Connection Error", e.getMessage());
            return false;

        }

    }


}

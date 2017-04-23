package com.hackust.createastore;

import android.util.Log;

import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Tavish on 22-Apr-17.
 */

public class ApiClient {
    private static String baseUrl="10.89.3.161";
    private static int port= 8082;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static Call addUser(RequestBody body)
    {
        HttpUrl httpUrl= new HttpUrl.Builder()
                .scheme("http")
                .host(baseUrl)
                .port(port)
                .addPathSegment("newStore.php")
                .build();
        Log.i("Api Client",httpUrl.toString());
        Request request= new Request.Builder()
                .post(body)
                .method("POST", body)
                .url(httpUrl)
                .build();
        return OkHttpSingleton.getOkHttpInstance().getOkHttpClient().newCall(request);
    }
    public static Call addPhoto(RequestBody body)
    {
        HttpUrl httpUrl= new HttpUrl.Builder()
                .scheme("http")
                .host(baseUrl)
                .port(port)
                .addPathSegment("newInventory.php")
                .build();
        Log.i("Api Client",httpUrl.toString());
        Request request= new Request.Builder()
                .post(body)
                .method("POST", body)
                .url(httpUrl)
                .build();
        return OkHttpSingleton.getOkHttpInstance().getOkHttpClient().newCall(request);
    }
    public static Call getItem(RequestBody body)
    {
        HttpUrl httpUrl= new HttpUrl.Builder()
                .scheme("http")
                .host(baseUrl)
                .port(port)
                .addPathSegment("getInventory.php")
                .build();
        Log.i("Api Client",httpUrl.toString());
        Request request= new Request.Builder()
                .post(body)
                .method("POST", body)
                .url(httpUrl)
                .build();
        return OkHttpSingleton.getOkHttpInstance().getOkHttpClient().newCall(request);
    }
}

package com.hackust.createastore;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by Tavish on 22-Apr-17.
 */

public class OkHttpSingleton {
    private static OkHttpSingleton okHttpSingleton;
    private OkHttpClient okHttpClient;

    private OkHttpSingleton() {

        okHttpClient = new OkHttpClient.Builder()
                .build();

    }

    public static OkHttpSingleton getOkHttpInstance() {
        if (okHttpSingleton == null) {

            return new OkHttpSingleton();
        }
        return okHttpSingleton;
    }
    public OkHttpClient getOkHttpClient()
    {
        return okHttpClient;
    }
}

package com.github.lexasoft.masstats;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.github.lexasoft.masstats.api.ApiService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {
    private static final String BASE_URL = "http://masstat.ru:8085/api/";

    private static ApiService service;
    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        service = retrofit.create(ApiService.class);
    }

    public static ApiService getApi() {
        return service;
    }

    public static void Log(String TAG, String message) {
        Toast.makeText(App.getContext(), message, Toast.LENGTH_SHORT);
        Log.d(TAG, message);
    }
}

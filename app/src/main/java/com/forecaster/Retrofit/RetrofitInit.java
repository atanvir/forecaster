package com.forecaster.Retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitInit {
//    String API_BASE_URL = "http://18.188.1.136:4002/api/v1/user/";
    String API_BASE_URL ="http://18.218.65.12:4002/api/v1/user/";
    private RetroInterface methods;
    private static RetrofitInit connect;
    public static synchronized RetrofitInit getConnect(){
        if (connect==null){
            connect=new RetrofitInit();
        }
        return connect;
    }

    public RetroInterface createConnection()  {

        if (methods == null)
        {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();//    logs HTTP request and response data.
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);//  set your desired log level
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.connectTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(100, TimeUnit.SECONDS);
//            .writeTimeout(15, TimeUnit.SECONDS);
//            // add your other interceptors …
            httpClient.addInterceptor(logging); //  add logging as last interceptor

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();

            methods = retrofit.create(RetroInterface.class);
        }
        return methods;
    }

}

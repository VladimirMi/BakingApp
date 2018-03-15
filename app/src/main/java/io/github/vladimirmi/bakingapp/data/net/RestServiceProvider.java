package io.github.vladimirmi.bakingapp.data.net;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * Provider for {@link RestService}.
 */

public class RestServiceProvider {

    private final static String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";
    private final static int CONNECT_TIMEOUT = 5000;
    private final static int READ_TIMEOUT = 5000;
    private final static int WRITE_TIMEOUT = 5000;

    private RestServiceProvider() {
    }

    public static RestService getService(OkHttpClient client) {
        return createRetrofit(client).create(RestService.class);
    }

    public static OkHttpClient createClient() {
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
                .build();
    }

    private static Retrofit createRetrofit(OkHttpClient okHttp) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(createConvertFactory())
                .client(okHttp)
                .build();
    }

    private static Converter.Factory createConvertFactory() {
        return MoshiConverterFactory.create();
    }
}

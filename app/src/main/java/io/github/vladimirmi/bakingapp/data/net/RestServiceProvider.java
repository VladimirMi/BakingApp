package io.github.vladimirmi.bakingapp.data.net;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * Provider for {@link RestService}.
 */

public class RestServiceProvider {

    private final static String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private final static int CONNECT_TIMEOUT = 5000;
    private final static int READ_TIMEOUT = 5000;
    private final static int WRITE_TIMEOUT = 5000;

    private RestServiceProvider() {
    }

    public static RestService getService() {
        return createRetrofit(createClient()).create(RestService.class);
    }

    private static OkHttpClient createClient() {
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
                .build();
    }

    private static Retrofit createRetrofit(OkHttpClient okHttp) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(createConvertFactory())
                .client(okHttp)
                .build();
    }

    private static Converter.Factory createConvertFactory() {
        return MoshiConverterFactory.create();
    }
}

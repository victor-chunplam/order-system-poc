package com.victor.backend.projects.orderSystem.config.restClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.victor.backend.projects.orderSystem.restClient.GoogleMapRestApiRepository;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

@Configuration
public class GoogleMapRestApiRetrofitConfig {

    @Value("${google.map.api.uri.domain}")
    String apiDomain = "";

    @Bean
    public GoogleMapRestApiRepository init() {

        Gson gsonFormatter = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        OkHttpClient.Builder httpClient = new OkHttpClient
                .Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY));

        return new Retrofit.Builder()
                .baseUrl(apiDomain) //TODO:
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create(gsonFormatter))
                .build()
                .create(GoogleMapRestApiRepository.class);
    }

}


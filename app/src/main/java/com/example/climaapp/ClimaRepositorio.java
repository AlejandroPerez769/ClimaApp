package com.example.climaapp;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClimaRepositorio {
    private ClimaApi weatherApi;

    public ClimaRepositorio() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        weatherApi = retrofit.create(ClimaApi.class);
    }

    public Call<ClimaResponse> getWeather(String city, String apiKey) {
        return weatherApi.getWeather(city, "us", apiKey, "json");
    }
}


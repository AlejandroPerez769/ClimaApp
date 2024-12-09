package com.example.climaapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

// API https://www.visualcrossing.com/weather/weather-data-services# cys6*!QLgz#9q3!

public interface ClimaApi {
    @GET("{city}")
    Call<ClimaResponse> getWeather(
            @Path("city") String city,
            @Query("unitGroup") String unitGroup, // Métrico o imperial
            @Query("key") String apiKey,
            @Query("contentType") String contentType // Tipo de respuesta JSON
    );
}
package com.example.climaapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClimaViewModel extends ViewModel {
    private MutableLiveData<ClimaResponse> weatherData = new MutableLiveData<>();
    private ClimaRepositorio repository;

    public ClimaViewModel() {
        repository = new ClimaRepositorio();
    }

    public LiveData<ClimaResponse> getWeatherData() {
        return weatherData;
    }

    public void fetchWeather(String city, String apiKey) {
        repository.getWeather(city, apiKey).enqueue(new Callback<ClimaResponse>() {
            @Override
            public void onResponse(Call<ClimaResponse> call, Response<ClimaResponse> response) {
                if (response.isSuccessful()) {
                    weatherData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ClimaResponse> call, Throwable t) {
                weatherData.setValue(null);
            }
        });
    }
}

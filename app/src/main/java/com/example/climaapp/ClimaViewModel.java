package com.example.climaapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ClimaViewModel: Clase que actúa como intermediaria entre la vista y los datos relacionados con el clima.
 * Utiliza LiveData para notificar a las vistas sobre cambios en los datos observados.
 */
public class ClimaViewModel extends ViewModel {
    // LiveData que contiene los datos del clima. Es observado por las vistas.
    private MutableLiveData<ClimaResponse> weatherData = new MutableLiveData<>();

    // Repositorio que proporciona acceso a los datos de clima.
    private ClimaRepositorio repository;

    /**
     * Constructor: Inicializa el repositorio.
     */
    public ClimaViewModel() {
        repository = new ClimaRepositorio();
    }

    /**
     * Devuelve un LiveData con los datos del clima para que puedan ser observados por la vista.
     *
     * @return LiveData<ClimaResponse> Los datos del clima.
     */
    public LiveData<ClimaResponse> getWeatherData() {
        return weatherData;
    }

    /**
     * Método para obtener datos de clima llamando al repositorio.
     * Actualiza weatherData con la respuesta de la API.
     *
     * @param city   Nombre de la ciudad para la consulta.
     * @param apiKey Clave API para autenticar la solicitud.
     */
    public void fetchWeather(String city, String apiKey) {
        // Llamada a la API a través del repositorio.
        repository.getWeather(city, apiKey).enqueue(new Callback<ClimaResponse>() {
            @Override
            public void onResponse(Call<ClimaResponse> call, Response<ClimaResponse> response) {
                // Si la respuesta es exitosa, actualiza LiveData con los datos obtenidos.
                if (response.isSuccessful()) {
                    weatherData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ClimaResponse> call, Throwable t) {
                // En caso de error, establece el valor de LiveData como null.
                weatherData.setValue(null);
            }
        });
    }
}

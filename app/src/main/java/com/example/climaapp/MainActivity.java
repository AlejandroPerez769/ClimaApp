package com.example.climaapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    private ClimaViewModel viewModel;
    private TextView cityTextView;
    private EditText searchEditText;
    private ImageView searchIcon;
    private TextView tempTextView;
    private TextView tempMinMax;
    private TextView descrp;
    private ImageView weatherIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar vistas
        cityTextView = findViewById(R.id.cityTextView);
        searchEditText = findViewById(R.id.searchEditText);
        searchIcon = findViewById(R.id.search);
        tempTextView = findViewById(R.id.tempTextView);
        tempMinMax = findViewById(R.id.tempMinMax);
        descrp = findViewById(R.id.descrp);
        weatherIcon = findViewById(R.id.weatherIcon);

        // Inicializar ViewModel
        viewModel = new ViewModelProvider(this).get(ClimaViewModel.class);

        // Observar cambios en los datos del clima
        viewModel.getWeatherData().observe(this, weatherResponse -> {
            if (weatherResponse != null && weatherResponse.getDays() != null && weatherResponse.getDays().size() > 0) {
                String fullResolvedAddress = weatherResponse.getResolvedAddress();
                String[] addressParts = fullResolvedAddress.split(",");
                String city = addressParts[0].trim();

                // Actualizar datos en las vistas
                cityTextView.setText(city);
                descrp.setText(weatherResponse.getDescription());

                double tempInFahrenheit = weatherResponse.getDays().get(0).getTemp();
                double tempInCelsius = (tempInFahrenheit - 32) * 5 / 9;

                tempTextView.setText(String.format("%.1f°C", tempInCelsius));

                double tempMinInFahrenheit = weatherResponse.getDays().get(0).getTempMin();
                double tempMaxInFahrenheit = weatherResponse.getDays().get(0).getTempMax();

                double tempMinInCelsius = (tempMinInFahrenheit - 32) * 5 / 9;
                double tempMaxInCelsius = (tempMaxInFahrenheit - 32) * 5 / 9;

                tempMinMax.setText(String.format("Min: %.1f°C / Max: %.1f°C", tempMinInCelsius, tempMaxInCelsius));

                String condition = weatherResponse.getDays().get(0).getConditions();
                boolean isNight = esNoche(weatherResponse.getDays().get(0).getSunrise(), weatherResponse.getDays().get(0).getSunset(), weatherResponse.getTimezone());

                if (condition.contains("Sunny")) {
                    weatherIcon.setImageResource(isNight ? R.drawable.night_clear_icon : R.drawable.sunny_icon);
                } else if (condition.contains("Overcast")) {
                    weatherIcon.setImageResource(isNight ? R.drawable.night_cloudy_icon : R.drawable.cloudy_icon);
                } else if (condition.contains("Rain")) {
                    weatherIcon.setImageResource(isNight ? R.drawable.rainy_night : R.drawable.rainy_icon);
                }
                else if (condition.contains("Snow")) {
                    weatherIcon.setImageResource(isNight ? R.drawable.night_snow : R.drawable.snowy_icon);
                }
                else {
                    weatherIcon.setImageResource(isNight ? R.drawable.night_clear_icon : R.drawable.sunny_icon);
                }
            }
        });

        // Búsqueda inicial
        viewModel.fetchWeather("Sucre", "H2AQ7DADRVHEAHUTSVTQ53GRU");

        // Configurar barra de búsqueda
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String city = s.toString().trim();
                if (!city.isEmpty()) {
                    viewModel.fetchWeather(city, "H2AQ7DADRVHEAHUTSVTQ53GRU");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE)) {
                String city = searchEditText.getText().toString().trim();
                if (!city.isEmpty()) {
                    viewModel.fetchWeather(city, "H2AQ7DADRVHEAHUTSVTQ53GRU");
                    toggleSearchBar();
                }
                return true;
            }
            return false;
        });

        searchIcon.setOnClickListener(v -> toggleSearchBar());
    }

    private void toggleSearchBar() {
        if (searchEditText.getVisibility() == View.GONE) {
            cityTextView.setVisibility(View.GONE);
            searchEditText.setVisibility(View.VISIBLE);
            searchEditText.requestFocus();
        } else {
            searchEditText.setVisibility(View.GONE);
            cityTextView.setVisibility(View.VISIBLE);
        }
    }

    /**
     *
     * @param sunrise La hora del amanecer
     * @param sunset La hora del atardecer
     * @param timezone La zona horaria, por ejemplo "America/Los Angeles"
     * @return Devuelve un booleano, si es verdadero la hora de lugar es antes del amanecer y despues del anochecer
     * por lo que será de noche. Si es falso, será de día
     */

    private boolean esNoche(String sunrise, String sunset, String timezone) {
        try {

            //Para conseguir el formato de la hora Local.getdefault y formatear los objetos
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            // Para cambiar la zona la cual hemos buscado, cambiamos el timezone, asi nos devolverá la información
            //del lugar
            timeFormat.setTimeZone(TimeZone.getTimeZone(timezone));

            //Obtenemos la hora del amanecer y la hora del anochecer
            Date sunriseTime = timeFormat.parse(sunrise);
            Date sunsetTime = timeFormat.parse(sunset);
            //Nos permite obtener la hora  de la instancia obteniendo la nuestra
            Calendar calendar = Calendar.getInstance();
            //Sin embargo, como hemos introducido la zona horaria al usar timeformat formateamos la hora
            // cambindo nuestra hora a la hora del país
            Date localTime = timeFormat.parse(timeFormat.format(calendar.getTime()));

            //Devuelve true si es de noche, antes del amanecer o despues del anochecer
            return localTime.before(sunriseTime) || localTime.after(sunsetTime);

        } catch (ParseException e) {
            Log.e("ClimaApp", "Error al analizar las horas: " + e.getMessage());
            return false;
        }
    }



}

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    private ClimaViewModel viewModel;
    private TextView cityTextView, horario;
    private EditText searchEditText;
    private ImageView searchIcon;
    private TextView tempTextView;
    private TextView tempMinMax;
    private TextView descrp;
    private ImageView weatherIcon;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityTextView = findViewById(R.id.cityTextView);
        searchEditText = findViewById(R.id.searchEditText);
        searchIcon = findViewById(R.id.search);
        tempTextView = findViewById(R.id.tempTextView);
        tempMinMax = findViewById(R.id.tempMinMax);
        descrp = findViewById(R.id.descrp);
        weatherIcon = findViewById(R.id.weatherIcon);
        recyclerView = findViewById(R.id.recyclerView);
        horario = findViewById(R.id.hora);


        viewModel = new ViewModelProvider(this).get(ClimaViewModel.class);

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

                String condition = weatherResponse.getDays().get(0).getIcon();
                boolean isNight = esNoche(weatherResponse.getDays().get(0).getSunrise(), weatherResponse.getDays().get(0).getSunset(), weatherResponse.getTimezone());

                if (condition.contains("sunny")) {
                    weatherIcon.setImageResource(isNight ? R.drawable.night_clear_icon : R.drawable.sunny_icon);
                } else if (condition.contains("overcast") || condition.contains("cloudy")) {
                    weatherIcon.setImageResource(isNight ? R.drawable.night_cloudy_icon : R.drawable.cloudy);
                } else if (condition.contains("rain")) {
                    weatherIcon.setImageResource(isNight ? R.drawable.rainy_night : R.drawable.rainy_icon);
                }
                else if (condition.contains("snow")) {
                    weatherIcon.setImageResource(isNight ? R.drawable.night_snow : R.drawable.snowy_icon);
                }
                else {
                    weatherIcon.setImageResource(isNight ? R.drawable.night_clear_icon : R.drawable.sunny_icon);
                }

                List<Integer> imagenes = new ArrayList<>();
                List<String> textoHora = new ArrayList<>();
                List<String> textoTemp = new ArrayList<>();

                /**
                 * @currentDay Obtiene toda la informacion del día de hoy
                 * Existe un array llamado horas que tiene las horas de cada día con la
                 * información del tiempo
                 */
                ClimaResponse.Day currentDay = weatherResponse.getDays().get(0);
                List<ClimaResponse.Hour> horas = currentDay.getHours();



                /**
                 * @horas Es una lista con todas las horas
                 * @weather.getTimezome() obtenemos la zona horaria
                 * Por ejemplo "Europe/Madrid"
                 * Esto es introducido en el objeto TimeZone.getTimeZone para
                 * obtener la zona horaria y lo metemos en una variable
                 * @timeZone
                 *
                 */

                TimeZone timeZone = TimeZone.getTimeZone(weatherResponse.getTimezone());
                /**
                 * @timeFormat para crear el formato de la hora, es decir,
                 * como queremos que se muestre la hora
                 */
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                timeFormat.setTimeZone(timeZone);

                /**
                 * @calendar Nos ayudará a obtener la hora
                 */
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeZone(timeZone); //Aquí ya hemos obtenido la hora de la zona horaria

                //Ej 17:00
                /**
                 * @hora extraemos solo la hora
                 */
                int hora = calendar.get(Calendar.HOUR_OF_DAY);
                int minuto = calendar.get(Calendar.MINUTE);
                horario.setText(hora + ":" + minuto);

                for (ClimaResponse.Hour hour : horas) {
                    try {
                        String hourFormatted = hour.getDatetime().substring(0, 5);

                        // Añadir todas las horas sin filtrar
                        textoHora.add(hourFormatted);
                        double tempInCelsiuss = (hour.getTemp() - 32) * 5 / 9;
                        textoTemp.add(String.format("%.1f°C", tempInCelsiuss));
                        
                        Date sunriseTime = timeFormat.parse(currentDay.getSunrise());
                        Date sunsetTime = timeFormat.parse(currentDay.getSunset());
                        Date localTime = timeFormat.parse(hourFormatted);

                        boolean isNightt = localTime.before(sunriseTime) || localTime.after(sunsetTime);

                        String icono = hour.getIcon();
                        if (icono.contains("sunny") || icono.contains("clear")) {
                            imagenes.add(isNightt ? R.drawable.night_clear_icon : R.drawable.sunny_icon);
                        } else if (icono.contains("cloudy") || icono.contains("overcast") || icono.contains("partially cloudy")) {
                            imagenes.add(isNightt ? R.drawable.night_cloudy_icon : R.drawable.cloudy);
                        } else if (icono.contains("rain")) {
                            imagenes.add(isNightt ? R.drawable.rainy_night : R.drawable.rainy_icon);
                        } else if (icono.contains("snow")) {
                            imagenes.add(isNightt ? R.drawable.night_snow : R.drawable.snowy_icon);
                        } else {
                            imagenes.add(isNightt ? R.drawable.night_clear_icon : R.drawable.sunny_icon);
                        }



                    } catch (ParseException e) {
                        Log.e("ClimaApp", "Error al procesar la hora: " + e.getMessage());
                    }
                }



                LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(layoutManager);

                CardAdapter adapter = new CardAdapter(imagenes, textoHora, textoTemp);
                recyclerView.setAdapter(adapter);


            }
        });
        viewModel.fetchWeather("Sucre", "H2AQ7DADRVHEAHUTSVTQ53GRU");


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
                    desaparecerBarra();
                }
                return true;
            }
            return false;
        });

        searchIcon.setOnClickListener(v -> desaparecerBarra());
    }

    /**
     * Este metodo es utilizado para cuando apretemos al boton de buscar
     * aparezca el buscados para introducir un nombre.
     * Cuando es introducido, desaparece la barra y aparece el nombre de la ciudad
     * introducida.
     */
    private void desaparecerBarra() {
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

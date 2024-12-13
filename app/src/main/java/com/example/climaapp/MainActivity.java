package com.example.climaapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;


import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    private ClimaViewModel viewModel;
    private EditText searchEditText;
    private ImageView searchIcon;
    private TextView tempTextView, tempMinMax, descrp, cityTextView, horario;
    private TextView amanecerTextView, anochecerTextView;
    private TextView velocidadTextView, rafagaTextView, uvTextView;
    private TextView presionTextView, radiationTextView, sunEnergyTextView;
    private ImageView weatherIcon;
    private RecyclerView recyclerView;
    Toolbar toolbar;


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
        amanecerTextView = findViewById(R.id.amanecer);
        anochecerTextView = findViewById(R.id.anochecer);
        rafagaTextView = findViewById(R.id.rafaga);
        velocidadTextView = findViewById(R.id.velocidad);
        presionTextView = findViewById(R.id.presion);
        sunEnergyTextView = findViewById(R.id.solarEnergy);
        radiationTextView = findViewById(R.id.radiation);
        uvTextView = findViewById(R.id.radiationUV);

        /**
         * Esta es la variable más importante de todo el programa.
         * Se utiliza para acceder al ViewModel que maneja la lógica de datos
         * del clima y proporciona datos a la vista.
         */
        viewModel = new ViewModelProvider(this).get(ClimaViewModel.class);

/**
 * Observador que se activa cuando hay cambios en los datos del clima.
 * Cuando se recibe una respuesta de clima, se actualizan las vistas
 * correspondientes con la nueva información.
 *
 * @param weatherResponse La respuesta de la API de clima que contiene datos sobre el clima.
 */
        viewModel.getWeatherData().observe(this, weatherResponse -> {
            if (weatherResponse != null && weatherResponse.getDays() != null && weatherResponse.getDays().size() > 0) {
                // Obtener la dirección completa resuelta de la respuesta
                String fullResolvedAddress = weatherResponse.getResolvedAddress();
                String[] addressParts = fullResolvedAddress.split(","); // Separar dirección por comas
                String city = addressParts[0].trim(); // Obtener la ciudad y eliminar espacios

                // Actualizar datos en las vistas
                cityTextView.setText(city); // Mostrar ciudad en la vista
                descrp.setText(weatherResponse.getDescription()); // Mostrar descripción del clima

                // Convertir temperatura de Fahrenheit a Celsius
                double tempInFahrenheit = weatherResponse.getDays().get(0).getTemp();
                double tempInCelsius = (tempInFahrenheit - 32) * 5 / 9;

                tempTextView.setText(String.format("%.1f°C", tempInCelsius)); // Mostrar temperatura actual en Celsius

                double tempMinInFahrenheit = weatherResponse.getDays().get(0).getTempMin();
                double tempMaxInFahrenheit = weatherResponse.getDays().get(0).getTempMax();

                double tempMinInCelsius = (tempMinInFahrenheit - 32) * 5 / 9; // Convertir mínima a Celsius
                double tempMaxInCelsius = (tempMaxInFahrenheit - 32) * 5 / 9; // Convertir máxima a Celsius

                // Mostrar temperaturas mínima y máxima en Celsius
                tempMinMax.setText(String.format("Min: %.1f°C / Max: %.1f°C", tempMinInCelsius, tempMaxInCelsius));

                String condition = weatherResponse.getDays().get(0).getIcon(); // Obtener condición climática
                boolean isNight = esNoche(weatherResponse.getDays().get(0).getSunrise(), weatherResponse.getDays().get(0).getSunset(), weatherResponse.getTimezone());

                // Actualizar información del amanecer y atardecer
                amanecerTextView.setText(weatherResponse.getDays().get(0).getSunrise());
                anochecerTextView.setText(weatherResponse.getDays().get(0).getSunset());
                velocidadTextView.setText("Velocidad de viento: " + weatherResponse.getDays().get(0).getWindspeed());
                rafagaTextView.setText("Fuerza del viento: " + weatherResponse.getDays().get(0).getWindgust());
                presionTextView.setText("Presión atmosférica: " + weatherResponse.getDays().get(0).getPressure());
                sunEnergyTextView.setText("Energia solar: " + weatherResponse.getDays().get(0).getSolarEnergy());
                radiationTextView.setText("Radiación: " + weatherResponse.getDays().get(0).getSolarRadiation());
                uvTextView.setText("Radiación UV: " + weatherResponse.getDays().get(0).getUvindex());

                // Asignar icono del clima según la condición y si es de noche
                if (condition.contains("sunny")) {
                    weatherIcon.setImageResource(isNight ? R.drawable.night : R.drawable.sunny_icon);
                } else if (condition.contains("overcast") || condition.contains("cloudy")) {
                    weatherIcon.setImageResource(isNight ? R.drawable.night_cloudy_icon : R.drawable.cloudy);
                } else if (condition.contains("rain")) {
                    weatherIcon.setImageResource(isNight ? R.drawable.rainy_night : R.drawable.rainy_icon);
                } else if (condition.contains("snow")) {
                    weatherIcon.setImageResource(isNight ? R.drawable.night_snow : R.drawable.snowy_icon);
                } else {
                    weatherIcon.setImageResource(isNight ? R.drawable.night : R.drawable.sunny_icon);
                }

                // Listas para almacenar datos de horas y temperaturas
                List<Integer> imagenes = new ArrayList<>();
                List<String> textoHora = new ArrayList<>();
                List<String> textoTemp = new ArrayList<>();

                /**
                 * Obtiene toda la información del día de hoy.
                 * Existe un array llamado horas que tiene las horas de cada día con la
                 * información del tiempo.
                 */
                ClimaResponse.Day currentDay = weatherResponse.getDays().get(0);
                List<ClimaResponse.Hour> horas = currentDay.getHours();

                /**
                 * Se obtiene la zona horaria de la respuesta del clima.
                 * Por ejemplo "Europe/Madrid" se convierte en un objeto TimeZone.
                 */
                TimeZone timeZone = TimeZone.getTimeZone(weatherResponse.getTimezone());

                /**
                 * Para crear el formato de la hora, se define cómo queremos que se muestre.
                 */
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                timeFormat.setTimeZone(timeZone);

                /**
                 * Se crea un calendario para manejar la hora local.
                 */
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeZone(timeZone); // Establecer la zona horaria en el calendario

                // Extraer la hora y minuto actuales
                int hora = calendar.get(Calendar.HOUR_OF_DAY);
                int minuto = calendar.get(Calendar.MINUTE);

                // Mostrar la hora actual en la vista
                horario.setText(String.format("%02d:%02d", hora, minuto));

                /**
                 * Iterar sobre todas las horas para obtener
                 * información para el "RecyclerView".
                 * Cada hora, tiempo y temperatura de esa hora se agregan a sus listas correspondientes.
                 */
                for (ClimaResponse.Hour hour : horas) {
                    try {
                        String hourFormatted = hour.getDatetime().substring(0, 5); // Extraer solo la hora

                        textoHora.add(hourFormatted); // Agregar hora a la lista
                        double tempInCelsiuss = (hour.getTemp() - 32) * 5 / 9; // Convertir temperatura a Celsius
                        textoTemp.add(String.format("%.1f°C", tempInCelsiuss)); // Agregar temperatura a la lista

                        // Obtener horas de amanecer y atardecer para determinar si es de noche
                        Date sunriseTime = timeFormat.parse(currentDay.getSunrise());
                        Date sunsetTime = timeFormat.parse(currentDay.getSunset());
                        Date localTime = timeFormat.parse(hourFormatted);

                        boolean isNightt = localTime.before(sunriseTime) || localTime.after(sunsetTime);

                        String icono = hour.getIcon();
                        // Asignar icono según la condición del clima y si es de noche
                        if (icono.contains("sunny") || icono.contains("clear")) {
                            imagenes.add(isNightt ? R.drawable.night : R.drawable.sunny_icon);
                        } else if (icono.contains("cloudy") || icono.contains("overcast") || icono.contains("partially cloudy")) {
                            imagenes.add(isNightt ? R.drawable.night_cloudy_icon : R.drawable.cloudy);
                        } else if (icono.contains("rain")) {
                            imagenes.add(isNightt ? R.drawable.rainy_night : R.drawable.rainy_icon);
                        } else if (icono.contains("snow")) {
                            imagenes.add(isNightt ? R.drawable.night_snow : R.drawable.snowy_icon);
                        } else {
                            imagenes.add(isNightt ? R.drawable.night : R.drawable.sunny_icon);
                        }

                    } catch (ParseException e) {
                        Log.e("ClimaApp", "Error al procesar la hora: " + e.getMessage());
                    }
                }

                // Configurar el RecyclerView para mostrar las horas y temperaturas
                LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(layoutManager); // Configurar el layout manager

                CardAdapter adapter = new CardAdapter(imagenes, textoHora, textoTemp); // Crear el adaptador
                recyclerView.setAdapter(adapter); // Establecer el adaptador en el RecyclerView
            }
        });

        viewModel.fetchWeather("Sucre", "H2AQ7DADRVHEAHUTSVTQ53GRU");

        /**
         * Configuración del campo de texto para buscar ciudades.
         * Utiliza el evento "addTextChangeListener" para definir las acciones
         * que se realizarán cuando el texto en la barra de búsqueda cambie.
         */
        searchEditText.addTextChangedListener(new TextWatcher() {

            /**
             * Método llamado antes de que el texto cambie.
             * Se puede utilizar para realizar acciones antes de que se produzca el cambio.
             * En este caso, no se realiza ninguna acción.
             *
             * @param s     El carácter que se está cambiando.
             * @param start La posición inicial del texto que se está cambiando.
             * @param count La cantidad de caracteres que se están eliminando.
             * @param after La cantidad de caracteres que se están agregando.
             */
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No se necesita implementación aquí
            }

            /**
             * Método llamado cuando el texto cambia.
             * Aquí es donde se realiza la búsqueda de la ciudad ingresada.
             *
             * @param s     El texto que se ha cambiado.
             * @param start La posición inicial del texto cambiado.
             * @param before La cantidad de caracteres que se estaban en el texto antes del cambio.
             * @param count La cantidad de caracteres que se están agregando al texto.
             */
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String city = s.toString().trim(); // Convierte el texto a un String y elimina espacios en blanco
                if (!city.isEmpty()) {
                    // Llama al método del ViewModel para buscar el clima de la ciudad ingresada
                    viewModel.fetchWeather(city, "H2AQ7DADRVHEAHUTSVTQ53GRU");
                }
            }

            /**
             * Método llamado después de que el texto ha cambiado.
             * Se puede utilizar para realizar acciones después de que el cambio ha ocurrido.
             * En este caso, no se realiza ninguna acción.
             *
             * @param s El texto que se ha cambiado.
             */
            @Override
            public void afterTextChanged(Editable s) {
                // No se necesita implementación aquí
            }
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

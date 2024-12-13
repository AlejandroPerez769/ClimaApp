package com.example.climaapp;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Clase principal que representa la respuesta de la API del clima.
 */
public class ClimaResponse {
    private String resolvedAddress; // Direccion resuelta
    private String description; // Descripcion del clima
    private List<Day> days; // Lista de objetos Day que representan el clima por dia
    private int tzoffset; // Desfase horario
    private String timezone; // Zona horaria

    /**
     * Devuelve la direccion resuelta.
     *
     * @return Direccion resuelta como una cadena.
     */
    public String getResolvedAddress() {
        return resolvedAddress;
    }

    /**
     * Devuelve la zona horaria.
     *
     * @return Zona horaria como una cadena.
     */
    public String getTimezone() {
        return timezone;
    }

    /**
     * Devuelve la descripcion del clima.
     *
     * @return Descripcion del clima como una cadena.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Devuelve el desfase horario.
     *
     * @return Desfase horario como un entero.
     */
    public int getTzoffset() {
        return tzoffset;
    }

    /**
     * Devuelve la lista de dias con informacion del clima.
     *
     * @return Lista de objetos Day.
     */
    public List<Day> getDays() {
        return days;
    }

    /**
     * Clase interna que representa el clima por dia.
     */
    public static class Day {
        private double temp; // Temperatura actual
        private double tempmax; // Temperatura maxima
        private double tempmin; // Temperatura minima
        private String conditions; // Condiciones climaticas
        private String sunrise; // Hora de salida del sol
        private String sunset; // Hora de puesta del sol
        private int timezoneOffset; // Desfase horario
        private String timezone; // Zona horaria
        private int datetimeEpoch; // Fecha y hora en formato epoch
        private List<Hour> hours; // Lista de objetos Hour con informacion horaria
        private String icon; // Icono del clima

        @SerializedName("windgust")
        private double windGust; // Rafaga de viento

        @SerializedName("windspeed")
        private double windSpeed; // Velocidad del viento

        private double pressure; // Presion atmosferica

        @SerializedName("solarradiation")
        private double solarRadiation; // Radiacion solar

        @SerializedName("solarenergy")
        private double solarEnergy; // Energia solar

        private double uvindex; // Indice UV

        /**
         * Devuelve el indice UV.
         *
         * @return Indice UV como un double.
         */
        public double getUvindex() {
            return uvindex;
        }

        /**
         * Devuelve la temperatura maxima.
         *
         * @return Temperatura maxima como un double.
         */
        public double getTempmax() {
            return tempmax;
        }

        /**
         * Devuelve la energia solar.
         *
         * @return Energia solar como un double.
         */
        public double getSolarEnergy() {
            return solarEnergy;
        }

        /**
         * Devuelve la radiacion solar.
         *
         * @return Radiacion solar como un double.
         */
        public double getSolarRadiation() {
            return solarRadiation;
        }

        /**
         * Devuelve la presion atmosferica.
         *
         * @return Presion atmosferica como un double.
         */
        public double getPressure() {
            return pressure;
        }

        /**
         * Devuelve la rafaga de viento.
         *
         * @return Rafaga de viento como un double.
         */
        public double getWindgust() {
            return windGust;
        }

        /**
         * Devuelve la velocidad del viento.
         *
         * @return Velocidad del viento como un double.
         */
        public double getWindspeed() {
            return windSpeed;
        }

        /**
         * Devuelve el icono del clima.
         *
         * @return Icono del clima como una cadena.
         */
        public String getIcon() {
            return icon;
        }

        /**
         * Devuelve la temperatura actual.
         *
         * @return Temperatura actual como un double.
         */
        public double getTemp() {
            return temp;
        }

        /**
         * Devuelve la temperatura maxima.
         *
         * @return Temperatura maxima como un double.
         */
        public double getTempMax() {
            return tempmax;
        }

        /**
         * Devuelve la temperatura minima.
         *
         * @return Temperatura minima como un double.
         */
        public double getTempMin() {
            return tempmin;
        }

        /**
         * Devuelve las condiciones climaticas.
         *
         * @return Condiciones climaticas como una cadena.
         */
        public String getConditions() {
            return conditions;
        }

        /**
         * Devuelve la hora de salida del sol.
         *
         * @return Hora de salida del sol como una cadena.
         */
        public String getSunrise() {
            return sunrise;
        }

        /**
         * Devuelve la hora de puesta del sol.
         *
         * @return Hora de puesta del sol como una cadena.
         */
        public String getSunset() {
            return sunset;
        }

        /**
         * Devuelve el desfase horario.
         *
         * @return Desfase horario como un entero.
         */
        public int getTimezoneOffset() {
            return timezoneOffset;
        }

        /**
         * Devuelve la zona horaria.
         *
         * @return Zona horaria como una cadena.
         */
        public String getTimezone() {
            return timezone;
        }

        /**
         * Devuelve la fecha y hora en formato epoch.
         *
         * @return Fecha y hora en formato epoch como un entero.
         */
        public int getDatetimeEpoch() {
            return datetimeEpoch;
        }

        /**
         * Devuelve la lista de horas con informacion climatica.
         *
         * @return Lista de objetos Hour.
         */
        public List<Hour> getHours() {
            return hours;
        }
    }

    /**
     * Clase interna que representa la informacion climatica por hora.
     */
    public static class Hour {
        private String datetime; // Fecha y hora
        private double temp; // Temperatura
        private String conditions; // Condiciones climaticas
        private String icon; // Icono del clima

        /**
         * Devuelve el icono del clima.
         *
         * @return Icono del clima como una cadena.
         */
        public String getIcon() {
            return icon;
        }

        /**
         * Devuelve la fecha y hora.
         *
         * @return Fecha y hora como una cadena.
         */
        public String getDatetime() {
            return datetime;
        }

        /**
         * Devuelve la temperatura.
         *
         * @return Temperatura como un double.
         */
        public double getTemp() {
            return temp;
        }

        /**
         * Devuelve las condiciones climaticas.
         *
         * @return Condiciones climaticas como una cadena.
         */
        public String getConditions() {
            return conditions;
        }
    }
}

package com.example.climaapp;

import java.util.List;

public class ClimaResponse {
    private String resolvedAddress;
    private String description;
    private List<Day> days;
    private int tzoffset;
    private String timezone;

    // Getters y Setters
    public String getResolvedAddress() {
        return resolvedAddress;
    }

    public String getTimezone() {
        return timezone;
    }

    public String getDescription() {
        return description;
    }

    public int getTzoffset() {
        return tzoffset;
    }

    public List<Day> getDays() {
        return days;
    }

    public static class Day {
        private double temp;
        private double tempMax;
        private double tempMin;
        private String conditions;
        private String sunrise;
        private String sunset;
        private int timezoneOffset;
        private String timezone;
        private int datetimeEpoch;

        public int getDatetimeEpoch() {
            return datetimeEpoch;
        }

        public String getTimezone() {
            return timezone;
        }

        // Getters
        public double getTemp() {
            return temp;
        }

        public double getTempMax() {
            return tempMax;
        }

        public double getTempMin() {
            return tempMin;
        }

        public String getConditions() {
            return conditions;
        }

        public String getSunrise() {
            return sunrise;
        }

        public String getSunset() {
            return sunset;
        }

        public int getTimezoneOffset() {
            return timezoneOffset;
        }
    }

}

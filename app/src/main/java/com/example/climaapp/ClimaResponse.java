package com.example.climaapp;

import com.google.gson.annotations.SerializedName;

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
        private double tempmax;
        private double tempmin;
        private String conditions;
        private String sunrise;
        private String sunset;
        private int timezoneOffset;
        private String timezone;
        private int datetimeEpoch;
        private List<Hour> hours;
        private String icon;
        @SerializedName("windgust")
        private double windGust;
        @SerializedName("windspeed")
        private double windSpeed;
        private double pressure;
        @SerializedName("solarradiation")
        private double solarRadiation;
        @SerializedName("solarenergy")
        private double solarEnergy;
        private double uvindex;

        public double getUvindex() {
            return uvindex;
        }

        public double getTempmax() {
            return tempmax;
        }

        public double getSolarEnergy() {
            return solarEnergy;
        }

        public double getSolarRadiation() {
            return solarRadiation;
        }

        public double getPressure() {
            return pressure;
        }
        public double getWindgust() {
            return  windGust;
        }

        public double getWindspeed() {
            return  windSpeed;
        }

        public String getIcon() {
            return icon;
        }

        public double getTemp() {
            return temp;
        }

        public double getTempMax() {
            return tempmax;
        }

        public double getTempMin() {
            return tempmin;
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

        public String getTimezone() {
            return timezone;
        }

        public int getDatetimeEpoch() {
            return datetimeEpoch;
        }

        public List<Hour> getHours() {
            return hours;
        }
    }

    public static class Hour {
        private String datetime;
        private double temp;
        private String conditions;
        private String icon;


        public String getIcon() {
            return icon;
        }
        public String getDatetime() {
            return datetime;
        }

        public double getTemp() {
            return temp;
        }

        public String getConditions() {
            return conditions;
        }

    }
}

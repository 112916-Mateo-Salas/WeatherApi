package ar.edu.utn.frc.tup.lciii.dtos;

import lombok.Data;

@Data
public class ForecastResponse {
    private Long id;
    private String name;
    private String country;
    private WeatherInfo weather;

    @Data
    public static class WeatherInfo {
        private String main;
        private String description;
    }
}
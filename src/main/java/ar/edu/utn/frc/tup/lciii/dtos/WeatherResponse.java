package ar.edu.utn.frc.tup.lciii.dtos;

import lombok.Data;

import java.util.List;

@Data
public class WeatherResponse {
    private List<WeatherApiResponse> weather;
}

package ar.edu.utn.frc.tup.lciii.services;

import ar.edu.utn.frc.tup.lciii.dtos.ForecastResponse;
import ar.edu.utn.frc.tup.lciii.dtos.TemperatureResponse;
import ar.edu.utn.frc.tup.lciii.models.Weather;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WeatherService {
    TemperatureResponse getTemperature(String city);
    ForecastResponse getForecast(String city);
    List<Weather> getAllWeather(String country, String unit);

}

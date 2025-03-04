package ar.edu.utn.frc.tup.lciii.controllers;


import ar.edu.utn.frc.tup.lciii.services.WeatherRiskService;
import ar.edu.utn.frc.tup.lciii.services.WeatherService;
import ar.edu.utn.frc.tup.lciii.dtos.WeatherAlertResponse;
import ar.edu.utn.frc.tup.lciii.dtos.WeatherApiResponse;
import ar.edu.utn.frc.tup.lciii.services.WeatherRiskService;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ar.edu.utn.frc.tup.lciii.dtos.ForecastResponse;
import ar.edu.utn.frc.tup.lciii.dtos.TemperatureResponse;
import ar.edu.utn.frc.tup.lciii.models.Weather;
import ar.edu.utn.frc.tup.lciii.services.WeatherService;

import java.util.List;

@RestController
@RequestMapping("/clima")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private WeatherRiskService weatherRiskService;

    @PostMapping("temperatura")
    public ResponseEntity<TemperatureResponse> getTemperature(@RequestParam("city") String cityName) {
        // TODO  Completar los parametros del endpoint
        //       Completar el llamado al servicio
        TemperatureResponse response = weatherService.getTemperature(cityName);
        if(response == null){
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/pronostico")
    public ResponseEntity<ForecastResponse> getForecast(@RequestParam String city) {
        // TODO  Completar los parametros del endpoint
        //       Completar el llamado al servicio
        return ResponseEntity.ok(weatherService.getForecast(city));
    }

    @GetMapping()
    public ResponseEntity<List<Weather>> getAllWeather(@RequestParam(value = "country", required = false) String countryCode,
                                                       @RequestParam(value = "unit") String unit) {
        // TODO  Completar los parametros del endpoint
        //       Completar el llamado al servicio

        return ResponseEntity.ok(weatherService.getAllWeather(countryCode, unit));
    }

    @GetMapping("/alertas/{country}")
    public ResponseEntity<WeatherAlertResponse> getWeatherAlerts(@RequestParam String country) {
        // TODO  Completar los parametros del endpoint
        //       Completar el llamado al servicio
        return ResponseEntity.ok(weatherRiskService.analyzeCountryRisks(country));
    }

}

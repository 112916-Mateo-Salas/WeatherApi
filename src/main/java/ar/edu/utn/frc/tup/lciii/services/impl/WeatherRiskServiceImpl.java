package ar.edu.utn.frc.tup.lciii.services.impl;

import ar.edu.utn.frc.tup.lciii.dtos.WeatherAlertResponse;
import ar.edu.utn.frc.tup.lciii.models.Weather;
import ar.edu.utn.frc.tup.lciii.dtos.WeatherAlertResponse.*;

import ar.edu.utn.frc.tup.lciii.repositories.WeatherRepository;
import ar.edu.utn.frc.tup.lciii.services.WeatherRiskService;
import ar.edu.utn.frc.tup.lciii.util.TemperatureConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class WeatherRiskServiceImpl implements WeatherRiskService {

    @Autowired
    private WeatherRepository weatherRepository;
    @Autowired
    private TemperatureConverter temperatureConverter;

    @Override
    public WeatherAlertResponse analyzeCountryRisks(String country) {
        List<Weather> cities = weatherRepository.findByCountry(country);
        if (cities.isEmpty()) {
            throw new RuntimeException("No weather data found for country: " + country);
        }

        List<CityRiskInfo> cityRisks = new ArrayList<>();

        for (Weather city : cities) {
            CityRiskInfo riskInfo = analyzeCityRisk(city);
            cityRisks.add(riskInfo);
        }

        CityRiskInfo highestRiskCity = cityRisks.stream()
                .max(Comparator.comparing(CityRiskInfo::getRiskScore))
                .orElseThrow();

        WeatherAlertResponse response = new WeatherAlertResponse();
        response.setCountry(country);
        response.setHighestRiskCity(highestRiskCity);
        response.setAllCitiesRisk(cityRisks);
        response.setAlertMessage(generateAlertMessage(highestRiskCity));

        return response;
    }

    private CityRiskInfo analyzeCityRisk(Weather city) {
        // TODO   Completar el metodo
        //        completar el analisis de riesgo, Riesgos por temperatura y Riesgos por Clima
        //        tener en cuenta la tabla de valores para calcular el RiskScore
        //        retornar el CityRiskInfo calculado

        CityRiskInfo cityRisk = new CityRiskInfo();
        RiskFactor riskFactor = new RiskFactor();
        int score = 0;
        double temp = city.getTempCelsius();
        switch (city.getWeatherCondition()){
            case "Clear" :{
                if(temp>=35) {
                    riskFactor.setDescription("Temperatura extremadamente alta (>35°C)");
                    riskFactor.setFactor("Temperatura");
                    riskFactor.setSeverity("ALTA");
                    score += 3;
                } else if (temp<=30) {
                    riskFactor.setDescription("Temperatura elevada (30-35°C)");
                    riskFactor.setFactor("Temperatura");
                    riskFactor.setSeverity("MEDIA");
                    score +=2;
                }
            }; break;
            case "Rain":{
                riskFactor.setDescription("Condiciones lluviosas que pueden causar inconvenientes");
                riskFactor.setFactor("LLUVIA");
                riskFactor.setSeverity("MEDIA");
                score +=2;

            }break;
            case "Thunderstorm": {
                riskFactor.setDescription("Tormentas eléctricas que pueden ser peligrosas");
                riskFactor.setFactor("TORMENTA");
                riskFactor.setSeverity("ALTA");
                score +=3;

            }break;
            case "Snow": {
                riskFactor.setDescription("Condiciones nevadas que pueden afectar la movilidad");
                riskFactor.setFactor("NIEVE");
                riskFactor.setSeverity("MEDIA");
                score +=2;
            }break;
            default: break;
        }
        return cityRisk;
    }

    private String generateAlertMessage(CityRiskInfo highestRiskCity) {
        if (highestRiskCity.getRiskScore() == 0) {
            return "No se detectaron riesgos significativos en ninguna ciudad.";
        }

        StringBuilder message = new StringBuilder();
        message.append("¡Alerta! ").append(highestRiskCity.getCityName())
                .append(" presenta el mayor riesgo climático.\n");

        for (RiskFactor risk : highestRiskCity.getRiskFactors()) {
            message.append("- ").append(risk.getDescription())
                    .append(" (Severidad: ").append(risk.getSeverity()).append(")\n");
        }

        return message.toString();
    }
}


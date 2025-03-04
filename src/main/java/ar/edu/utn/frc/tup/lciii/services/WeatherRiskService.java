package ar.edu.utn.frc.tup.lciii.services;

import ar.edu.utn.frc.tup.lciii.dtos.WeatherAlertResponse;
import org.springframework.stereotype.Service;

@Service
public interface WeatherRiskService {
    WeatherAlertResponse analyzeCountryRisks(String country);

}

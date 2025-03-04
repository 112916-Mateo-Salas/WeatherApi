package ar.edu.utn.frc.tup.lciii.services.impl;

import ar.edu.utn.frc.tup.lciii.repositories.WeatherRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class WeatherRiskServiceImplTest {

    @MockBean
    private WeatherRepository weatherRepository;

    @SpyBean
    private WeatherRiskServiceImpl weatherRiskService;

    @Test
    void analyzeCountryRisks() {
        when(weatherRepository.findByCountry("AR")).thenReturn(List.of());

        assertThrows(RuntimeException.class, () ->{
            weatherRiskService.analyzeCountryRisks("AR");
        });
    }
}
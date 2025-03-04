package ar.edu.utn.frc.tup.lciii.dtos;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Setter
@Getter
public class WeatherAlertResponse {
    private String country;
    private CityRiskInfo highestRiskCity;
    private List<CityRiskInfo> allCitiesRisk;
    private String alertMessage;

    @Data
    @Setter
    @Getter
    public static class CityRiskInfo {
        private String cityName;
        private double riskScore;
        private List<RiskFactor> riskFactors;
    }

    @Data
    @Setter
    @Getter
    public static class RiskFactor {
        private String factor;
        private String description;
        private String severity;
    }
}

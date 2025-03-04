package ar.edu.utn.frc.tup.lciii.models;


import ar.edu.utn.frc.tup.lciii.dtos.WeatherApiResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity(name = "Weather")
public class Weather {
    // TODO completar la clase para que gestione los atributos de la entidad CLIMA en la DB

    @Column
    private Long id;

    @Id
    private String name;

    @Column
    private String weatherCondition;
    @Column
    private String country;

    @Column
    private double feels_like;

    @Column
    private double tempCelsius;

    @Column
    private  double tempFahrenheit;

    @Column
    private  double tempKelvin;

}

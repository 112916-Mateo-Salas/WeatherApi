package ar.edu.utn.frc.tup.lciii.dtos;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class TemperatureResponse {
    // TODO  Completar los atributos de la DTO
    private Long id;

    private String name;

    private String country;

    private double temp;

    private double feels_like;
}

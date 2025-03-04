package ar.edu.utn.frc.tup.lciii.services.impl;

import ar.edu.utn.frc.tup.lciii.dtos.ForecastResponse;
import ar.edu.utn.frc.tup.lciii.dtos.TemperatureResponse;
import ar.edu.utn.frc.tup.lciii.dtos.WeatherApiResponse;
import ar.edu.utn.frc.tup.lciii.dtos.WeatherResponse;
import ar.edu.utn.frc.tup.lciii.models.Weather;
import ar.edu.utn.frc.tup.lciii.repositories.WeatherRepository;
import ar.edu.utn.frc.tup.lciii.services.WeatherService;
import ar.edu.utn.frc.tup.lciii.util.TemperatureConverter;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.util.Json;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.annotations.SecondaryRow;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class WeatherServiceImpl implements WeatherService {

    @Autowired
    private WeatherRepository weatherRepository;

    @Autowired
    private TemperatureConverter temperatureConverter;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public TemperatureResponse getTemperature(String city) {


        // TODO Completar el metodo
        // analizar el objeto recibido para realizar la conversion de temperaturas
        // persistir en DB
        // generar la respuesta
        //JSONObject
        try {
            boolean dataJson = getDataJson();
            if(dataJson){
                TemperatureResponse tempResponse = new TemperatureResponse();
                Optional<Weather> weatherDB = weatherRepository.getByName(city);
                if(weatherDB.isEmpty()){
                    return null;
                }
                Weather weather = weatherDB.get();
                tempResponse.setId(weather.getId());
                tempResponse.setTemp(weather.getTempCelsius());
                tempResponse.setCountry(weather.getCountry());
                tempResponse.setFeels_like(weather.getFeels_like());
                tempResponse.setName(weather.getName());
                return tempResponse;
            } else {
                return null;
            }

        } catch (Exception e) {
            throw new EntityNotFoundException(e);
        }
    }

    @Override
    public ForecastResponse getForecast(String city) {
        // TODO Completar el metodo
        // analizar el objeto recibido para realizar la conversion de temperaturas
        // analizar el clima para determinar el mensaje del clima  generateForecastMessage(weatherMain, tempCelsius)
        // persistir en DB
        // generar la respuesta
        boolean weatherData = getDataJson();
        if(weatherData){
            Optional<Weather> weatherDB = weatherRepository.getByName(city);
            if(weatherDB.isEmpty()){
                return null;
            }
            Weather weather = weatherDB.get();
            String message = generateForecastMessage(weather.getWeatherCondition(), weather.getTempCelsius());
            ForecastResponse forecastResponse = new ForecastResponse();
            forecastResponse.setId(weather.getId());
            ForecastResponse.WeatherInfo info = new ForecastResponse.WeatherInfo();
            info.setMain(weather.getWeatherCondition());
            info.setDescription(message);
            forecastResponse.setWeather(info);
            forecastResponse.setName(weather.getName());
            forecastResponse.setCountry(weather.getCountry());
            return forecastResponse;
        } else {
            return null;
        }

    }

    @Override
    public List<Weather> getAllWeather(String country, String unit) {
        if (!unit.matches("(?i)[CFK]")) {
            throw new IllegalArgumentException("Invalid temperature unit. Use C, F, or K");
        }
        // TODO   Completar el metodo
        // buscar en la DB el registro por country
        // generar la lista de respuesta teniendo en consideracion mostrar solo la temperatura solicitada
        // los restantes campos de temperatudeb quedar en null
        List<Weather> weatherList;
        if (country!= null){
            weatherList = weatherRepository.findByCountry(country);
        } else {
            weatherList = weatherRepository.findAll();
        }
        List<Weather> weathers= new ArrayList<>();
        weatherList.forEach(weather -> {
            Weather response = new Weather();
            response.setId(weather.getId());
            response.setName(weather.getName());
            response.setCountry(weather.getCountry());
            response.setFeels_like(weather.getFeels_like());
            if(unit.equals("C")){
                response.setTempCelsius(weather.getTempCelsius());
            } else if (unit.equals("F")) {
                response.setTempFahrenheit(weather.getTempFahrenheit());
            } else {
                response.setTempKelvin(weather.getTempKelvin());
            }
            weathers.add(response);
        });
        return weathers;
        //return null;
    }



    private String generateForecastMessage(String weather, double tempCelsius) {
        // TODO  Completar el metodo
        //       completar las validaciones segun la tabla
        //       respetar todos los mensajes
        String message ;
        switch (weather) {
            case "Clouds" :message = generateMessageClouds(tempCelsius);
                break;
            case "Rain": message = generateMessageRain(tempCelsius);
                break;
            case "Clear": message = generateMessageClear(tempCelsius);
                break;
            case "Haze": message = generateMessageHaze(tempCelsius);
                break;
            default: message = "Condición climática no especificada";
        }
        return message;
    }

    private String generateMessageClouds(double tempCelsius) {
        if(tempCelsius <=15){
            return "Día frío y nublado, abrígate bien.";
        } else if (tempCelsius <=25) {
            return "Clima templado y nublado, ideal para un paseo.";
        } else if (tempCelsius <=36) {
            return "Día cálido con nubes, mantente hidratado.";
        }else {
            return "Clima extremadamente caluroso con nubes, evita el sol directo.";
        }
    }

    private String generateMessageRain(double tempCelsius) {
        if(tempCelsius <=15){
            return "Lluvia y frío, lleva abrigo e impermeable.";
        } else if (tempCelsius <=25) {
            return "Lluvia ligera con clima templado, usa paraguas.";
        } else if (tempCelsius <=36) {
            return "Lluvia con calor, posible humedad alta.";
        }else {
            return "Lluvia en clima muy caluroso, posible tormenta.";
        }
    }

    private String generateMessageHaze(double tempCelsius) {
        if(tempCelsius <=15){
            return "Neblina y frío, maneja con precaución.";
        } else if (tempCelsius <=25) {
            return "Neblina ligera, visibilidad reducida.";
        } else if (tempCelsius <=36) {
            return "Ambiente cálido y brumoso, posibles alergias.";
        }else {
            return "Neblina densa con calor extremo, toma precauciones.";
        }
    }

    private String generateMessageClear(double tempCelsius) {
        if(tempCelsius <=15){
            return "Día soleado pero frío, abrígate bien.";
        } else if (tempCelsius <=25) {
            return "Clima perfecto, disfruta el día.";
        } else if (tempCelsius <=36) {
            return "Día soleado y caluroso, usa protector solar.";
        }else {
            return "Ola de calor, evita la exposición prolongada al sol.";
        }
    }

    protected boolean getDataJson(){
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data.json");
            if (inputStream == null) {
                throw new RuntimeException("Archivo data.json no encontrado en resources");
            }
            WeatherResponse weatherResponse = objectMapper.readValue(inputStream, WeatherResponse.class);
            List<WeatherApiResponse> weatherDatas = weatherResponse.getWeather();
            if(weatherDatas!= null){
                weatherDatas.forEach(weatherData ->{
                    Weather weather = new Weather();
                    weather.setId(weatherData.getId());
                    weather.setName(weatherData.getName());

                    weather.setCountry(weatherData.getCountry());
                    weather.setWeatherCondition(weatherData.getWeather().get(0).getMain());
                    double celsius = temperatureConverter.fahrenheitToCelsius(weatherData.getMain().getTemp());
                    weather.setTempCelsius(celsius);
                    weather.setTempFahrenheit(weatherData.getMain().getTemp());
                    double kelvin = temperatureConverter.fahrenheitToKelvin(weatherData.getMain().getTemp());
                    weather.setTempKelvin(kelvin);
                    weather.setFeels_like(weatherData.getMain().getFeels_like());
                    weatherRepository.save(weather);
                });
            }
            return true;
        } catch (IOException e) {
            new RuntimeException(e);
            return false;
        }
    }
}

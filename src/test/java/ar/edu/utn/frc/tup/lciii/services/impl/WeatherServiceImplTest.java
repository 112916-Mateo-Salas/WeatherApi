package ar.edu.utn.frc.tup.lciii.services.impl;

import ar.edu.utn.frc.tup.lciii.dtos.ForecastResponse;
import ar.edu.utn.frc.tup.lciii.dtos.TemperatureResponse;
import ar.edu.utn.frc.tup.lciii.dtos.WeatherAlertResponse;
import ar.edu.utn.frc.tup.lciii.models.Weather;
import ar.edu.utn.frc.tup.lciii.repositories.WeatherRepository;
import ar.edu.utn.frc.tup.lciii.util.TemperatureConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.aspectj.weaver.patterns.HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class WeatherServiceImplTest {

    @MockBean
    private WeatherRepository weatherRepository;

    @Autowired
    private TemperatureConverter temperatureConverter;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @SpyBean
    private WeatherServiceImpl weatherService;

    @Test
    void getTemperatureTest() {
        String city = "Cordoba";
        Weather weatherEntity = getWeather();
        when(weatherService.getDataJson()).thenReturn(true);
        when(weatherRepository.getByName(city)).thenReturn(Optional.of(weatherEntity));

        TemperatureResponse response = weatherService.getTemperature(city);
        assertEquals("AR",response.getCountry());
    }

    @Test
    void getTemperatureTestFallid() {
        String city = "Cordoba";
        Weather weatherEntity = getWeather();
        when(weatherService.getDataJson()).thenReturn(false);
        when(weatherRepository.getByName(city)).thenReturn(Optional.of(weatherEntity));

        TemperatureResponse response = weatherService.getTemperature(city);
        assertNull(response);
    }

    @Test
    void getTemperatureTestEmpty() {
        String city = "Cordoba";
        when(weatherService.getDataJson()).thenReturn(true);
        when(weatherRepository.getByName(city)).thenReturn(Optional.empty());

        TemperatureResponse response = weatherService.getTemperature(city);
        assertNull(response);
    }

    @Test
    void getTemperatureTestException() {
        String city = "Cordoba";
        Weather weatherEntity = getWeather();
        when(weatherService.getDataJson()).thenThrow(EntityNotFoundException.class);
        when(weatherRepository.getByName(city)).thenReturn(Optional.of(weatherEntity));

        assertThrows(EntityNotFoundException.class, () -> {
            weatherService.getTemperature(city);
        });
    }

    @Test
    void getForecastTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String city = "Cordoba";
        Weather weatherEntity = getWeather();
        when(weatherService.getDataJson()).thenReturn(true);
        when(weatherRepository.getByName(city)).thenReturn(Optional.of(weatherEntity));

        ForecastResponse response = weatherService.getForecast(city);
        assertEquals("AR",response.getCountry());

    }



    @Test
    void getForecastTestEmpty() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String city = "Cordoba";
        when(weatherService.getDataJson()).thenReturn(true);
        when(weatherRepository.getByName(city)).thenReturn(Optional.empty());

        ForecastResponse response = weatherService.getForecast(city);
        assertNull(response);
    }

    @Test
    void getForecastTestFakse() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String city = "Cordoba";
        when(weatherService.getDataJson()).thenReturn(false);

        ForecastResponse response = weatherService.getForecast(city);
        assertNull(response);
    }


    @Test
    void generateForecastMessageTestClouds() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = WeatherServiceImpl.class.getDeclaredMethod("generateForecastMessage", String.class, double.class);
        method.setAccessible(true);

        String result = (String) method.invoke(weatherService, "Clouds", 10.0);

        assertEquals("Día frío y nublado, abrígate bien.",result);
    }

    @Test
    void generateForecastMessageTestClouds20() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = WeatherServiceImpl.class.getDeclaredMethod("generateForecastMessage", String.class, double.class);
        method.setAccessible(true);

        String result = (String) method.invoke(weatherService, "Clouds", 20.0);

        assertEquals("Clima templado y nublado, ideal para un paseo.",result);
    }

    @Test
    void generateForecastMessageTestClouds30() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = WeatherServiceImpl.class.getDeclaredMethod("generateForecastMessage", String.class, double.class);
        method.setAccessible(true);

        String result = (String) method.invoke(weatherService, "Clouds", 30.0);

        assertEquals("Día cálido con nubes, mantente hidratado.",result);
    }

    @Test
    void generateForecastMessageTestClouds38() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = WeatherServiceImpl.class.getDeclaredMethod("generateForecastMessage", String.class, double.class);
        method.setAccessible(true);

        String result = (String) method.invoke(weatherService, "Clouds", 38.0);

        assertEquals("Clima extremadamente caluroso con nubes, evita el sol directo.",result);
    }

    @Test
    void generateForecastMessageTestRain() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = WeatherServiceImpl.class.getDeclaredMethod("generateForecastMessage", String.class, double.class);
        method.setAccessible(true);

        String result = (String) method.invoke(weatherService, "Rain", 10.0);

        assertEquals("Lluvia y frío, lleva abrigo e impermeable.",result);
    }

    @Test
    void generateForecastMessageTestRain20() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = WeatherServiceImpl.class.getDeclaredMethod("generateForecastMessage", String.class, double.class);
        method.setAccessible(true);

        String result = (String) method.invoke(weatherService, "Rain", 20.0);

        assertEquals("Lluvia ligera con clima templado, usa paraguas.",result);
    }

    @Test
    void generateForecastMessageTestRain30() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = WeatherServiceImpl.class.getDeclaredMethod("generateForecastMessage", String.class, double.class);
        method.setAccessible(true);

        String result = (String) method.invoke(weatherService, "Rain", 30.0);

        assertEquals("Lluvia con calor, posible humedad alta.",result);
    }

    @Test
    void generateForecastMessageTestRain38() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = WeatherServiceImpl.class.getDeclaredMethod("generateForecastMessage", String.class, double.class);
        method.setAccessible(true);

        String result = (String) method.invoke(weatherService, "Rain", 38.0);

        assertEquals("Lluvia en clima muy caluroso, posible tormenta.",result);
    }

    @Test
    void generateForecastMessageTestClear() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = WeatherServiceImpl.class.getDeclaredMethod("generateForecastMessage", String.class, double.class);
        method.setAccessible(true);

        String result = (String) method.invoke(weatherService, "Clear", 10.0);

        assertEquals("Día soleado pero frío, abrígate bien.",result);
    }

    @Test
    void generateForecastMessageTestClear20() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = WeatherServiceImpl.class.getDeclaredMethod("generateForecastMessage", String.class, double.class);
        method.setAccessible(true);

        String result = (String) method.invoke(weatherService, "Clear", 20.0);

        assertEquals("Clima perfecto, disfruta el día.",result);
    }

    @Test
    void generateForecastMessageTestClear30() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = WeatherServiceImpl.class.getDeclaredMethod("generateForecastMessage", String.class, double.class);
        method.setAccessible(true);

        String result = (String) method.invoke(weatherService, "Clear", 30.0);

        assertEquals("Día soleado y caluroso, usa protector solar.",result);
    }

    @Test
    void generateForecastMessageTestClear38() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = WeatherServiceImpl.class.getDeclaredMethod("generateForecastMessage", String.class, double.class);
        method.setAccessible(true);

        String result = (String) method.invoke(weatherService, "Clear", 38.0);

        assertEquals("Ola de calor, evita la exposición prolongada al sol.",result);
    }

    @Test
    void generateForecastMessageTestHaze() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = WeatherServiceImpl.class.getDeclaredMethod("generateForecastMessage", String.class, double.class);
        method.setAccessible(true);

        String result = (String) method.invoke(weatherService, "Haze", 10.0);

        assertEquals("Neblina y frío, maneja con precaución.",result);
    }

    @Test
    void generateForecastMessageTestHaze20() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = WeatherServiceImpl.class.getDeclaredMethod("generateForecastMessage", String.class, double.class);
        method.setAccessible(true);

        String result = (String) method.invoke(weatherService, "Haze", 20.0);

        assertEquals("Neblina ligera, visibilidad reducida.",result);
    }

    @Test
    void generateForecastMessageTestHaze30() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = WeatherServiceImpl.class.getDeclaredMethod("generateForecastMessage", String.class, double.class);
        method.setAccessible(true);

        String result = (String) method.invoke(weatherService, "Haze", 30.0);

        assertEquals("Ambiente cálido y brumoso, posibles alergias.",result);
    }

    @Test
    void generateForecastMessageTestHaze38() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = WeatherServiceImpl.class.getDeclaredMethod("generateForecastMessage", String.class, double.class);
        method.setAccessible(true);

        String result = (String) method.invoke(weatherService, "Haze", 38.0);

        assertEquals("Neblina densa con calor extremo, toma precauciones.",result);
    }

    @Test
    void generateForecastMessageTestNotDefined() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = WeatherServiceImpl.class.getDeclaredMethod("generateForecastMessage", String.class, double.class);
        method.setAccessible(true);

        String result = (String) method.invoke(weatherService, "NN", 10.0);

        assertEquals("Condición climática no especificada",result);
    }

    @Test
    void getAllWeatherTestCelsius() {
        String country = "AR";
        Weather weather = getWeather();
        when(weatherRepository.findByCountry(country)).thenReturn(List.of(getWeather()));

        List<Weather> result = weatherService.getAllWeather(country,"C");

        assertNotNull(result);
    }

    @Test
    void getAllWeatherTestExcCountry() {
        Weather weather = getWeather();
        when(weatherRepository.findAll()).thenReturn(List.of(getWeather()));

        List<Weather> result = weatherService.getAllWeather(null,"C");

        assertNotNull(result);
    }

    @Test
    void getAllWeatherTestException () {
        assertThrows(IllegalArgumentException.class,() ->{
            weatherService.getAllWeather(null,"A");
        });
    }

    @Test
    void getAllWeatherTestKelvin() {
        String country = "AR";
        Weather weather = getWeather();
        when(weatherRepository.findByCountry(country)).thenReturn(List.of(getWeather()));

        List<Weather> result = weatherService.getAllWeather(country,"K");

        assertNotNull(result);
    }


    private Weather getWeather(){
        Weather weather = new Weather();
        weather.setId(1L);
        weather.setName("Cordoba");
        weather.setCountry("AR");
        weather.setFeels_like(76.748);
        weather.setTempCelsius(24.5);
        weather.setTempFahrenheit(75.506);
        weather.setTempKelvin(298.01);
        weather.setWeatherCondition("Rain");
        return weather;

    }
}
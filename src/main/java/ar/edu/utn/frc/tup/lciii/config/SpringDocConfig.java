package ar.edu.utn.frc.tup.lciii.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//Con esta clase lo que buscamos es configurar la documentacion de OpenApi de un Swagger que es una forma
//de documentación que existe para las apis
//Nos expone como funciona nuestra Api

@Configuration
/*
Se utiliza para enlazar automaticamente propiedades de configuraciòn con campos de una clase.
Permite una facil configuracion y acceso a las propiedades en un archivo de
configuracipon, como application.properties
 */
//@ConfigurationProperties(prefix = "app")
//@Data
public class SpringDocConfig {
//     private String url;
//     private String devName;
//     private String devEmail;


    @Value("${app.url}") private String url;
    @Value("${app.dev-name}") private String devName;
    @Value("${app.dev-email}") private String devEmail;

    /*
    Anotoacion que se utiliza para indicar que en un metodo en una clase de
    configuracion de Spring debe ser tratado como un bean y gestionado con el contenedor de Spring
     */
    @Bean
    public OpenAPI openApi(
            /*
            Esta anotacion se utiliza para inyectar valores de propiedades en los campos de una clase.
            Permite que las propiedades definidas en un archivo de configuración (como en application.properties) sean inyectadas
            en las variables correspondientes.
             */
            @Value("'@project.name@'") String appName,
            @Value("'@project.description@'") String appDescription,
            @Value("'@project.version@'") String appVersion){
        Info info = new Info()
                .title(appName)
                .version(appVersion)
                .description(appDescription)
                .contact(
                        new Contact()
                                .name(devName)
                                .email(devEmail));
        Server server = new Server()
                .url(url)
                .description(appDescription);
        return new OpenAPI()
                .components(new Components())
                .info(info)
                .addServersItem(server);
    }



//Bean que necesita OpenApi para generar la documentación:

//    @Bean
//    public ModelResolver modelResolver(ObjectMapper objectMapper) {
//        return new ModelResolver(objectMapper);
//    }

    @Bean
    public ModelResolver modelResolver(ObjectMapper objectMapper) {
        return new ModelResolver(objectMapper);
    }
}

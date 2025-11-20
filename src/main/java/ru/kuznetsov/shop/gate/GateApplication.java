package ru.kuznetsov.shop.gate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import ru.kuznetsov.shop.auth.config.KeycloakServiceConfiguration;
import ru.kuznetsov.shop.business.access.config.DataAccessConfig;
import ru.kuznetsov.shop.parameter.config.ParameterConfig;

@SpringBootApplication
@Import({DataAccessConfig.class, KeycloakServiceConfiguration.class, ParameterConfig.class})
public class GateApplication {

    public static void main(String[] args) {
        SpringApplication.run(GateApplication.class, args);
    }

}

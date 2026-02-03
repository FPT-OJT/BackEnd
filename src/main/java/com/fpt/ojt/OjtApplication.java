package com.fpt.ojt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "FPT OJT Backend",
                version = "1.0.0",
                description = "OJT Backend API documentations"
        )
)
@SecurityScheme(
    name = "tokenAuth",
    type = SecuritySchemeType.APIKEY,
    in = SecuritySchemeIn.HEADER,
    paramName = "Authorization"
)
public class OjtApplication {

    static void main(String[] args) {
        SpringApplication.run(OjtApplication.class, args);
    }

}

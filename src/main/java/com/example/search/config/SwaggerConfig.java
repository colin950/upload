package com.example.search.config;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import com.example.search.constants.Constant;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

@Configuration
@SecurityScheme(name= "bearerAuth", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "Bearer")
public class SwaggerConfig {

    @Bean
    public OperationCustomizer customGlobalHeaders() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            Parameter defaultHeader = new Parameter()
                    .in(ParameterIn.HEADER.toString())
                    .schema(new StringSchema())
                    .name(Constant.LANGUAGE_HEADER)
                    .description("언어 (en, ko)")
                    .required(false);

            operation.addParametersItem(defaultHeader);
            return operation;
        };
    }
}

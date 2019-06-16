package com.mk.orderbooks;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static java.util.stream.Stream.of;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private String[] acceptedPaths = {"order-books", "financial-instruments"};

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(path -> of(acceptedPaths).filter(acceptedPath -> path != null && path.contains(acceptedPath)).findFirst().isPresent())
                .build();
    }
}

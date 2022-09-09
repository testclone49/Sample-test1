
package com.accolite.pru.health.AuthApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import com.accolite.pru.health.AuthApp.annotation.CurrentUser;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .ignoredParameterTypes(CurrentUser.class)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.accolite.pru.health.AuthApp"))
                .paths(PathSelectors.regex("/api.*"))
                .build()
                .apiInfo(metaInfo());
    }

    private ApiInfo metaInfo() {
        return new ApiInfoBuilder()
                .title("SWebrest Rest API")
                .description("swebrest User API Documentation Generated Using SWAGGER2")
				.termsOfServiceUrl("https://sample.com")
				.license("SWebrest Rest API")
				.licenseUrl("")
				.version("1.0")
                .build();
    }

}

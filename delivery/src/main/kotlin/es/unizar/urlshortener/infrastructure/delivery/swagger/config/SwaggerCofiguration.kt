package es.unizar.urlshortener.infrastructure.delivery.swagger.config

import springfox.documentation.spi.DocumentationType

/**
 * Swagger creates a web interface to document and use exposed application methods.
 * Several annotations can be used to customize the messages and inputs.
 * To access the page, go to use "RouteToWebApp/swagger-ui.html, where RouteToWebApp is where the
 * the applications is deployed. In a local case, it would be localhost:8080.
 */

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    // Don't forget the @Bean annotation
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .apiInfo(ApiInfo())
            .select()
            .apis(RequestHandlerSelectors.basePackage("es.unizar.urlshortener.infrastructure.delivery"))
            .paths(PathSelectors.any())
            .build()
    }

    private fun ApiInfo(): ApiInfo {
        return ApiInfoBuldier()
            .contact(Contact("Grupo A - UrlShortener", "https://github.com/Davidzf21/urlshortener", "780500@unizar.es"))
            .description("Trabajo de Ingenieria Web - Grupo A - 2022/2023")
            .licence("Apache License 2.0")
            .licenceUrl("https://www.apache.org/licenses/LICENSE-2.0")
            .title("URLSHORTENER - Grupo A")
            .version("1.0")
            .build()
    }
}
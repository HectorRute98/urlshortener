package es.unizar.urlshortener.infrastructure.delivery

import es.unizar.urlshortener.core.ClickProperties
import es.unizar.urlshortener.core.ShortUrlProperties
import es.unizar.urlshortener.core.usecases.CreateShortUrlUseCase
import es.unizar.urlshortener.core.usecases.InfoClientUserCase
import es.unizar.urlshortener.core.usecases.LogClickUseCase
import es.unizar.urlshortener.core.usecases.RedirectUseCase
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import java.net.URI
import javax.servlet.http.HttpServletRequest

/**
 * The specification of the controller.
 */
interface InfoClientController {

    /**
     * Devuelve información relevante sobre la URI acortada identificada por el parámetro id..
     *
     * **Note**:
     */
    fun infoner(id: String, request: HttpServletRequest): String

}

/**
 * The implementation of the controller.
 *
 * **Note**: Spring Boot is able to discover this [RestController] without further configuration.
 */
@Controller
class InfoClientControllerImpl (
        val infoClientUserCase: InfoClientUserCase
) : InfoClientController {

    @RequestMapping("/apiiiiii/link/{id}")
    override fun infoner(@PathVariable id: String, request: HttpServletRequest): String {
        val info = infoClientUserCase.getInfo(id)
        if (info != null) {
            for (i in info) {
                println(i.date + ", " + i.browser + ", " + i.platform)
            }
        }
        return "info"
    }
}
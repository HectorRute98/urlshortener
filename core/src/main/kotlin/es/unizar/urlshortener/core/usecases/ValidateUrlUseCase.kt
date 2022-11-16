package es.unizar.urlshortener.core.usecases

import es.unizar.urlshortener.core.*
import org.springframework.web.client.RestTemplate
import kotlinx.coroutines.*
import org.springframework.beans.factory.annotation.Autowired

/**
 * Dada una URL comprueba se es alcanzable y segura mediante la
 * herramienta de Google Safe Browse.
 *
 * **Note**: This is an example of functionality.
 */
enum class ValidateUrlResponse {
    OK,
    NO_REACHABLE
}

enum class ValidateUrlState {
    VALIDATION_ACEPT,
    VALIDATION_IN_PROGRESS,
    VALIDATION_FAIL_NOT_REACHABLE,
    VALIDATION_FAIL_NOT_SAFE,
    VALIDATION_NOT_DONE
}

interface ValidateUrlUseCase {
    suspend fun ValidateURL(url: String): ValidateUrlResponse
    suspend fun ReachableURL(url: String): ValidateUrlResponse
    suspend fun SafeURL(url: String): ValidateUrlResponse
}

/**
 * Implementation of [ValidateUrlResponse].
 */
class ValidateUrlUseCaseImpl(
) : ValidateUrlUseCase {

    @Autowired
    lateinit var restTemplate: RestTemplate

    @Value("\${google.API.clientName}")
    lateinit var googleClient: String
    @Value("\${google.API.clientVersion}")
    lateinit var googleVersion: String
    @Value("\${google.API.url}")
    lateinit var googleUrl: String
    @Value("\${google.API.value}")
    lateinit var googleValue: String

    /*** Comprobacion en paralelo y con corutanas de que la URL es segura y alcanzable ***/
    override suspend fun ValidateURL(url: String): ValidateUrlResponse = coroutineScope {
        var response1 = async { ReachableURL(url) }
        response1
        // LO MISMO PERO CON GOOGLE SAFE BROSING
        //if(response1.await() == ValidateUrlResponse.OK)
    }

    /*** Validacion de que la URL es alcanzable ***/
    override suspend fun ReachableURL(url: String): ValidateUrlResponse = {
        return try {
            var resp = restTemplate.getForEntity(url, String::class.java)
            if(resp.getStatusCode().is2xxSuccessful()) {
                ValidateUrlResponse.OK
            } else {
                ValidateUrlResponse.NO_REACHABLE
            }
        } catch (e: Exception){
            ValidateUrlResponse.NO_REACHABLE
        }
    }

    /*** Validacion de que la URL es segura con Google Safe Browse ***/
    override suspend fun SafeURL(url: String): ValidateUrlResponse = {
        // TODO
        ValidateUrlResponse.OK
    }


}
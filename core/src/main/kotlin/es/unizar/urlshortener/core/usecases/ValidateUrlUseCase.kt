package es.unizar.urlshortener.core.usecases

import es.unizar.urlshortener.core.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.client.RestTemplate;

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
    fun ValidateURL(url: String): ValidateUrlResponse
    fun ReachableURL(url: String): ValidateUrlResponse
    fun SafeURL(url: String): ValidateUrlResponse
    fun CheckBlockListURL(url: String): ValidateUrlResponse
}

/**
 * Implementation of [ValidateUrlResponse].
 */
class ValidateUrlUseCaseImpl(
) : ValidateUrlUseCase {

    @Autowired
    lateinit var restTemplate: RestTemplate

    lateinit var googleClient: String
    lateinit var googleVersion: String
    lateinit var googleUrl: String
    lateinit var googleValue: String

    /*** Comprobacion en paralelo y con corutanas de que la URL es segura y alcanzable ***/
    override fun ValidateURL(url: String): ValidateUrlResponse {
        var response1 = ReachableURL(url)
        //var response1 = SafeURL(url)
        //var response1 = CheckBlockListURL(url)
        return response1
    }

    /*** Validacion de que la URL es alcanzable ***/
    override fun ReachableURL(url: String): ValidateUrlResponse {
        return try {
            var resp = restTemplate.getForEntity(url, String::class.java)
            if(resp.statusCode.is2xxSuccessful) {
                ValidateUrlResponse.OK
            } else {
                ValidateUrlResponse.NO_REACHABLE
            }
        } catch (e: Exception){
            ValidateUrlResponse.NO_REACHABLE
        }
    }

    /*** Validacion de que la URL es segura con Google Safe Browse ***/
    override fun SafeURL(url: String): ValidateUrlResponse {
        // TODO
        return ValidateUrlResponse.OK
    }

    /*** Validacion de que la URL no se encuentra en la lista de SPAM ***/
    override fun CheckBlockListURL(url: String): ValidateUrlResponse {
        // TODO
        return ValidateUrlResponse.OK
    }


}
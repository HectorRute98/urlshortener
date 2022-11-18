package es.unizar.urlshortener.core.usecases

import es.unizar.urlshortener.core.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.net.URI

/**
 * Dada una URL comprueba se es alcanzable y segura mediante la
 * herramienta de Google Safe Browse.
 *
 * **Note**: This is an example of functionality.
 */
enum class ValidateUrlResponse {
    OK,
    NO_REACHABLE,
    UNSAFE
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

    @Value("\${google.API.clientName}")
    lateinit var googleClient: String
    @Value("\${google.API.clientVersion}")
    lateinit var googleVersion: String
    @Value("\${google.API.url}")
    lateinit var googleUrl: String
    @Value("\${google.API.value}")
    lateinit var googleValue: String

    /*** Comprobacion en paralelo y con corutanas de que la URL es segura y alcanzable ***/
    override fun ValidateURL(url: String): ValidateUrlResponse {
        var response1 = ReachableURL(url)
        var response2 = SafeURL(url)
        //var response1 = CheckBlockListURL(url)
        return response2
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
        println(googleClient + "," + googleVersion+ "," +googleUrl+ "," +googleValue)

        val Request = ThreatMatchesFindRequestBody(
                ClientInfo(googleClient, googleVersion),
                ThreatInfo(
                        listOf(ThreatType.MALWARE,ThreatType.POTENTIALLY_HARMFUL_APPLICATION,ThreatType.UNWANTED_SOFTWARE),
                        listOf(PlatformType.ALL_PLATFORMS),
                        listOf(ThreatEntryType.URL),
                        listOf(ThreatEntry(url,ThreatEntryRequestType.URL))
                )
        )
        println(Request)
        val mapper = jacksonObjectMapper()
        val serializador = mapper.writeValueAsString(Request)
        println(serializador)
        //https://testsafebrowsing.appspot.com/s/malware.html UNSAFE EXAMPLE
        val httpResponse = restTemplate.postForObject(URI(googleUrl+googleValue), HttpEntity(serializador),ThreatMatchesFindResponseBody::class.java)
        if(!httpResponse?.matches.isNullOrEmpty()){
            return ValidateUrlResponse.UNSAFE
        }
        return ValidateUrlResponse.OK
    }

    /*** Validacion de que la URL no se encuentra en la lista de SPAM ***/
    override fun CheckBlockListURL(url: String): ValidateUrlResponse {
        // TODO
        return ValidateUrlResponse.OK
    }


}
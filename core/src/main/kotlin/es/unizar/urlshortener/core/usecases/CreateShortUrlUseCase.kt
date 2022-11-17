package es.unizar.urlshortener.core.usecases

import es.unizar.urlshortener.core.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired

/**
 * Given an url returns the key that is used to create a short URL.
 * When the url is created optional data may be added.
 *
 * **Note**: This is an example of functionality.
 */
interface CreateShortUrlUseCase {
    fun create(url: String, data: ShortUrlProperties): ShortUrl
}

/**
 * Implementation of [CreateShortUrlUseCase].
 */
class CreateShortUrlUseCaseImpl(
    private val shortUrlRepository: ShortUrlRepositoryService,
    private val validatorService: ValidatorService,
    private val hashService: HashService
) : CreateShortUrlUseCase {

    @Autowired
    lateinit var validateUrlUseCase: ValidateUrlUseCase

    override fun create(url: String, data: ShortUrlProperties): ShortUrl =
            if (validatorService.isValid(url)) {
                val validateResponse = validateUrlUseCase.ValidateURL(url)
                val id: String = hashService.hasUrl(url)
                var su = ShortUrl(
                        hash = id,
                        redirection = Redirection(target = url),
                        properties = ShortUrlProperties(
                                safe = data.safe,
                                ip = data.ip,
                                sponsor = data.sponsor
                        ),
                        validation = ValidateUrlState.VALIDATION_IN_PROGRESS
                )
                shortUrlRepository.save(su)
                /*** Comprobamos la validacion de la URL ***/
                if (validateResponse == ValidateUrlResponse.OK) {
                    if(shortUrlRepository.updateValidate(su.hash, ValidateUrlState.VALIDATION_ACEPT)){
                        su = shortUrlRepository.findByKey(su.hash)!!
                    } else {
                        throw UpdateValidationNoWork(url)
                    }
                }
                if (validateResponse == ValidateUrlResponse.NO_REACHABLE){
                    shortUrlRepository.deleteByKey(su.hash)
                }
                println(su)
                su
            } else {
                throw InvalidUrlException(url)
            }
}

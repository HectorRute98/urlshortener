package es.unizar.urlshortener.core.usecases

import es.unizar.urlshortener.core.*
import org.springframework.beans.factory.annotation.Autowired
import kotlinx.coroutines.*

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
            runBlocking {

                val validateResponse = async { validateUrlUseCase.ValidateURL(url) }

                val id: String = hashService.hasUrl(url)
                val su = ShortUrl(
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
                var job = GlobalScope.launch {
                    runBlocking {
                        if (validateResponse.await() == ValidateUrlResponse.OK) {
                            shortUrlRepository.updateValidate(su.hash, VALIDATION_ACEPT)
                        }
                        if (validateResponse.await() == ValidateUrlResponse.NO_REACHABLE){
                            shortUrlRepository.deleteByKey(su.hash)
                        }
                    }
                }
                job.cancel()
                su
            }
        } else {
            throw InvalidUrlException(url)
        }
}

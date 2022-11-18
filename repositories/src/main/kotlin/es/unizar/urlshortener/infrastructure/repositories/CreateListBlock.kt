package es.unizar.urlshortener.infrastructure.repositories

import es.unizar.urlshortener.core.*
import es.unizar.urlshortener.core.usecases.ValidateUrlResponse
import es.unizar.urlshortener.core.usecases.ValidateUrlState
import es.unizar.urlshortener.core.usecases.ValidateUrlUseCase
import org.springframework.beans.factory.annotation.Autowired

/**
 * Given an url returns the key that is used to create a short URL.
 * When the url is created optional data may be added.
 *
 * **Note**: This is an example of functionality.
 */
interface CreateListBlock {
    fun create(): Int
}

/**
 * Implementation of [CreateListBlockImpl].
 */
class CreateListBlockImpl(
) : CreateListBlock {

    override fun create(): Int {
        TODO("Not yet implemented")
        println("Lectura Ficheros")
        return 1
    }

}
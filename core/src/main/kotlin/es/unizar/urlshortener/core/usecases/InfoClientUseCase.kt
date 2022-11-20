package es.unizar.urlshortener.core.usecases

import es.unizar.urlshortener.core.ClickRepositoryService
import es.unizar.urlshortener.core.Redirection
import es.unizar.urlshortener.core.RedirectionNotFound
import es.unizar.urlshortener.core.ShortUrlRepositoryService

/**
 * Given a key returns a [Redirection] that contains a [URI target][Redirection.target]
 * and an [HTTP redirection mode][Redirection.mode].
 *
 * **Note**: This is an example of functionality.
 */
interface InfoClientUserCase {
    fun getInfo(key: String): String
}

/**
 * Implementation of [RedirectUseCase].
 */
class InfoClientUserCaseImpl(
        private val clickRepository: ClickRepositoryService
) : InfoClientUserCase {
    override fun getInfo(key: String): String {
        if(clickRepository.existHash(key)){
            clickRepository.findByHash(key)
            println(clickRepository.findByHash(key))
            return "XD"
        } else {
            return "No se ha hecho ningun click"
        }
    }
}
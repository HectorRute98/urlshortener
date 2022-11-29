package es.unizar.urlshortener.core.usecases

import org.springframework.web.multipart.MultipartFile
import java.nio.file.FileStore

/**
 * Given a file creates a short URL for each url in the file
 * and creates a new file with the short URL or the error occurred.
 */
interface CreateUrlsFromCsvUseCase {
    fun create(file: MultipartFile, remoteAddr: String): String
}

/**
 * Implementation of [CreateShortUrlsFromCsvUseCase].
 */
class CreateUrlsFromCsvUseCaseImpl(
        private var fileStorage: FileStore,
        private val createShortUrlUseCase: CreateShortUrlUseCase
) : CreateUrlsFromCsvUseCase {
    override fun create(file: MultipartFile, remoteAddr: String): String {
        return "FICHERO"
    }
}
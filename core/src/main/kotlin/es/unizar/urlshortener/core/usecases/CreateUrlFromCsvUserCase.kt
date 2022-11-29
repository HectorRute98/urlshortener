package es.unizar.urlshortener.core.usecases

import es.unizar.urlshortener.core.*
import org.springframework.web.multipart.MultipartFile

/**
 * Given a file creates a short URL for each url in the file
 * and creates a new file with the short URL or the error occurred.
 */
interface CreateUrlsFromCsvUseCase {
    fun create(file: MultipartFile, remoteAddr: String): ShortUrlFile
}

/**
 * Implementation of [CreateUrlsFromCsvUseCase].
 */
class CreateUrlsFromCsvUseCaseImpl(
        private var fileStorage: FileStore,
        private val createShortUrlUseCase: CreateShortUrlUseCase
) : CreateUrlsFromCsvUseCase {
    override fun create(file: MultipartFile, remoteAddr: String): ShortUrlFile {
        if(checkTypeFile(file)){
            throw InvalidFileType()
        }
        val shortUrlsFile = ArrayList<ShortUrl>()
        val nuevoNombre = "${fileStorage.generateName()}.csv"
        fileStorage.store(file, nuevoNombre)
        val lineas = fileStorage.readLines(nuevoNombre)
        for(line in lineas){
            val shortUrl = createShortUrlUseCase.create(
                    url = line,
                    data = ShortUrlProperties(
                            ip = remoteAddr,
                    )
            )
            shortUrlsFile.add(shortUrl)
        }
        return ShortUrlFile(nuevoNombre, shortUrlsFile)
    }

    private fun checkTypeFile(file: MultipartFile): Boolean {
        val nameSplitByPoint = file.originalFilename?.split(".")
        if (nameSplitByPoint == null || nameSplitByPoint[nameSplitByPoint.size-1] != "csv") {
            return true
        }
        return false
    }

}
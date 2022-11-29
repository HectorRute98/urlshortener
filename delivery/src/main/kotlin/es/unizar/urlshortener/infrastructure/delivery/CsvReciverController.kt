package es.unizar.urlshortener.infrastructure.delivery

import es.unizar.urlshortener.core.FileStore
import es.unizar.urlshortener.core.InfoClientResponse
import es.unizar.urlshortener.core.ValidateUrlState
import es.unizar.urlshortener.core.usecases.*
import org.apache.tomcat.util.http.fileupload.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

interface CsvReciverController {

    /**
     * Redirects and logs a short url identified by its [id].
     *
     * **Note**: Delivery of use cases [RedirectUseCase] and [LogClickUseCase].
     */
    fun uploadCsvPage(model: MutableMap<String, Any>): String

    /**
     * Creates a short url from details provided in [data].
     *
     * **Note**: Delivery of use case [CreateShortUrlUseCase].
     */
    fun processCsv(file: MultipartFile, uuid: String, request: HttpServletRequest,
                  response: HttpServletResponse)

}

@Controller
class CsvReciverControllerImpl(
        val createUrlFromCsvUseCase: CreateUrlsFromCsvUseCase
) : CsvReciverController {

    @Autowired
    lateinit var fileStorage: FileStore

    @GetMapping("/api/bulk")
    override fun uploadCsvPage(model: MutableMap<String, Any>): String {
        model["uuid"] = UUID.randomUUID().toString()
        return "uploadPage"
    }

    @PostMapping("/api/bulk")
    override fun processCsv(@RequestParam("file") file: MultipartFile,
                           @RequestParam("uuid") uuid: String, request: HttpServletRequest,
                           response: HttpServletResponse) {

        createUrlFromCsvUseCase.create(file, request.remoteAddr).let {
            val newLines = ArrayList<String>()
            for(i in 0 until it.urls.size){
                val originalURL = it.urls[i].redirection.target
                val shortURL = "http://localhost:8080/" + it.urls[i].hash
                var error = ""
                if (it.urls[i].validation.equals(ValidateUrlState.VALIDATION_FAIL_NOT_REACHABLE)){
                    error = "ERROR: VALIDATION_FAIL_NOT_REACHABLE"
                } else if (it.urls[i].validation.equals(ValidateUrlState.VALIDATION_FAIL_NOT_SAFE)){
                    error = "ERROR: VALIDATION_FAIL_NOT_SAFE"
                } else if (it.urls[i].validation.equals(ValidateUrlState.VALIDATION_FAIL_BLOCK)){
                    error = "ERROR: VALIDATION_FAIL_BLOCK"
                }
                newLines.add("$originalURL;$shortURL;$error")
            }
            fileStorage.overWriteFile(it.filename, newLines)
            val fileGenerated = fileStorage.loadFile(it.filename)
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"URLS.csv\"")
            response.contentType = "text/csv"
            response.status = HttpStatus.CREATED.value()
            IOUtils.copy(fileGenerated.inputStream, response.outputStream)
            response.outputStream.close()
            fileGenerated.inputStream.close()
            fileStorage.deleteFile(it.filename)
        }

    }

}
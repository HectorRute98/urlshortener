package es.unizar.urlshortener.infrastructure.delivery

import es.unizar.urlshortener.core.usecases.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import java.nio.file.FileStore
import java.util.*

@Controller
class CsvReciverController(
        val createUrlFromCsvUseCase: CreateUrlsFromCsvUseCase
) {

    @GetMapping("/api/bulk")
    fun csv(model: MutableMap<String, Any>): String {
        model["uuid"] = UUID.randomUUID().toString()
        return "uploadPage"
    }


}
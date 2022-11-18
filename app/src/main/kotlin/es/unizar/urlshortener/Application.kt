package es.unizar.urlshortener


import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.createFile
import kotlin.io.path.relativeTo

/**
 * The marker that makes this project a Spring Boot application.
 */
@SpringBootApplication
class UrlShortenerApplication

/**
 * The main entry point.
 */
fun main(args: Array<String>) {
    CreateListBlockMain()
    runApplication<UrlShortenerApplication>(*args)
}

fun CreateListBlockMain() {
    println(Paths.get(""))
    val path = Paths.get("repositories\\src\\main\\resources\\SPAM.txt")
    Files.lines(path, Charsets.UTF_8).forEach { println(it) }
}

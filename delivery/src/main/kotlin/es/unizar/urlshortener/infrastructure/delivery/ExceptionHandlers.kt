package es.unizar.urlshortener.infrastructure.delivery

import es.unizar.urlshortener.core.*
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter


@ControllerAdvice
class RestResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {

    @ResponseBody
    @ExceptionHandler(value = [InvalidUrlException::class])
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected fun invalidUrls(ex: InvalidUrlException) = ErrorMessage(HttpStatus.BAD_REQUEST.value(), ex.message)

    @ResponseBody
    @ExceptionHandler(value = [RedirectionNotFound::class])
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected fun redirectionNotFound(ex: RedirectionNotFound) = ErrorMessage(HttpStatus.NOT_FOUND.value(), ex.message)

    @ResponseBody
    @ExceptionHandler(value = [UpdateValidationNoWork::class])
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected fun updateValidationNoWork(ex: UpdateValidationNoWork) = ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.message)

    @ResponseBody
    @ExceptionHandler(value = [RedirectionNotSafeOrBlock::class])
    @ResponseStatus(HttpStatus.FORBIDDEN)
    protected fun redirectionNotSafe(ex: RedirectionNotSafeOrBlock) = ErrorMessage(HttpStatus.FORBIDDEN.value(), ex.message)

    @ResponseBody
    @ExceptionHandler(value = [ShortUrlNotSafe::class])
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected fun shortUrlNotSafe(ex: ShortUrlNotSafe) = ErrorMessage(HttpStatus.BAD_REQUEST.value(), ex.message)

    @ResponseBody
    @ExceptionHandler(value = [RedirectionNotReachable::class])
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected fun redirectionNotReachable(ex: RedirectionNotReachable) = ErrorMessage(HttpStatus.BAD_REQUEST.value(), ex.message)
}

data class ErrorMessage(
    val statusCode: Int,
    val error: String?,
    val timestamp: String = DateTimeFormatter.ISO_DATE_TIME.format(OffsetDateTime.now())
)
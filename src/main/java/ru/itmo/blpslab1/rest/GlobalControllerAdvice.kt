package ru.itmo.blpslab1.rest;

import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class GlobalControllerAdvice {

    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    fun onHttpMessageNotReadableException(exception: HttpMessageNotReadableException): String {
        return "Json parser error"
    }

//    @ExceptionHandler(Throwable::class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    fun onUnknownOthers(exception: Throwable): String {
//        return "Internal server error"
//    }
}

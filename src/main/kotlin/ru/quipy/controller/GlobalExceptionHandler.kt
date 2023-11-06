package ru.quipy.controller

import org.springdoc.api.ErrorMessage
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {
    class ErrorEntity(
        val message: String?
    )
    @ExceptionHandler
    fun errorHandler(e: Exception) : ResponseEntity<ErrorEntity> {
        val errEntity = ErrorEntity(e.message)
        return ResponseEntity(errEntity, HttpStatus.BAD_REQUEST)
    }
}
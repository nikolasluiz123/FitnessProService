package br.com.fitnesspro.service.exception.handler

import br.com.fitnesspro.service.config.request.EnumRequestAttributes
import br.com.fitnesspro.service.exception.BusinessException
import br.com.fitnesspro.shared.communication.responses.PersistenceServiceResponse
import jakarta.persistence.EntityNotFoundException
import jakarta.servlet.http.HttpServletRequest
import org.hibernate.TransactionException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(TransactionException::class)
    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    fun handlerServiceTimeout(
        exception: TransactionException,
        request: HttpServletRequest
    ): PersistenceServiceResponse {
        request.setAttribute(EnumRequestAttributes.REQUEST_EXCEPTION.name, exception)

        return PersistenceServiceResponse(
            code = HttpStatus.REQUEST_TIMEOUT.value(),
            error = "A requisição excedeu o tempo e a conexão com o serviço foi desfeita."
        )
    }

    @ExceptionHandler(EntityNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handlerEntityNotFound(
        exception: EntityNotFoundException,
        request: HttpServletRequest
    ): PersistenceServiceResponse {
        request.setAttribute(EnumRequestAttributes.REQUEST_EXCEPTION.name, exception)

        return PersistenceServiceResponse(
            code = HttpStatus.NOT_FOUND.value(),
            error = exception.message
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handlerFieldValidationExceptions(
        exception: MethodArgumentNotValidException,
        request: HttpServletRequest
    ): PersistenceServiceResponse {
        request.setAttribute(EnumRequestAttributes.REQUEST_EXCEPTION.name, exception)

        val errors = exception.bindingResult.fieldErrors.mapNotNull { it.defaultMessage }

        return PersistenceServiceResponse(
            code = HttpStatus.BAD_REQUEST.value(),
            error = errors.joinToString(separator = ", ")
        )
    }

    @ExceptionHandler(BusinessException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handlerValidationExceptions(
        exception: BusinessException,
        request: HttpServletRequest
    ): PersistenceServiceResponse {
        request.setAttribute(EnumRequestAttributes.REQUEST_EXCEPTION.name, exception)

        return PersistenceServiceResponse(
            code = HttpStatus.BAD_REQUEST.value(),
            error = exception.message
        )
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handlerExceptions(
        exception: Exception,
        request: HttpServletRequest
    ): PersistenceServiceResponse {
        request.setAttribute(EnumRequestAttributes.REQUEST_EXCEPTION.name, exception)

        return PersistenceServiceResponse(
            code = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error = exception.message ?: "Ocorreu um erro não tratado."
        )
    }
}
package br.com.fitnesspro.config.interceptors

import br.com.fitnesspro.service.logs.ExecutionsLogService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class LoggingInterceptor(
    private val logService: ExecutionsLogService
) : HandlerInterceptor {

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        logService.saveLogPreHandle(request)
        return true
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        logService.updateLogAfterCompletion(request, response, ex)
    }
}

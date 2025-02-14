package br.com.fitnesspro.service.config.advice

import br.com.fitnesspro.service.config.advice.gson.adapter.LocalDateTimeTypeAdapter
import br.com.fitnesspro.service.config.advice.gson.adapter.LocalDateTypeAdapter
import br.com.fitnesspro.service.config.advice.gson.adapter.LocalTimeTypeAdapter
import com.google.gson.GsonBuilder
import org.springframework.core.MethodParameter
import org.springframework.http.HttpInputMessage
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@ControllerAdvice
class FitnessProRequestBodyAdviceAdapter : RequestBodyAdviceAdapter() {

    override fun supports(
        methodParameter: MethodParameter,
        targetType: Type,
        converterType: Class<out HttpMessageConverter<*>>
    ): Boolean {
        return methodParameter.hasParameterAnnotation(RequestBody::class.java)
    }

    override fun afterBodyRead(
        body: Any,
        inputMessage: HttpInputMessage,
        parameter: MethodParameter,
        targetType: Type,
        converterType: Class<out HttpMessageConverter<*>>
    ): Any {
        val gson = GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter())
            .registerTypeAdapter(LocalDate::class.java, LocalDateTypeAdapter())
            .registerTypeAdapter(LocalTime::class.java, LocalTimeTypeAdapter())
            .create()

        val jsonData = gson.toJson(body)
        val requestAttributes = RequestContextHolder.getRequestAttributes() as ServletRequestAttributes
        requestAttributes.request.setAttribute("logData", jsonData)

        return body
    }
}
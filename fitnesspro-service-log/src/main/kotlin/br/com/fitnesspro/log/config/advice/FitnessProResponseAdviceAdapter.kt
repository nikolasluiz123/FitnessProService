package br.com.fitnesspro.log.config.advice

import br.com.fitnesspro.log.enums.EnumRequestAttributes
import br.com.fitnesspro.service.communication.gson.defaultServiceGSon
import com.google.gson.GsonBuilder
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

@ControllerAdvice
class FitnessProResponseAdviceAdapter: ResponseBodyAdvice<Any> {

    override fun supports(
        returnType: MethodParameter,
        converterType: Class<out HttpMessageConverter<*>?>
    ): Boolean {
        return true
    }

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>?>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): Any? {
        val gson = GsonBuilder().defaultServiceGSon()
        val jsonData = gson.toJson(body)
        val requestAttributes = RequestContextHolder.getRequestAttributes() as ServletRequestAttributes
        requestAttributes.request.setAttribute(EnumRequestAttributes.RESPONSE_DATA.name, jsonData)

        return body
    }
}
package br.com.fitnesspro.log.config.advice

import br.com.fitnesspro.core.gson.defaultGSon
import br.com.fitnesspro.log.enums.EnumRequestAttributes
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
        val gson = GsonBuilder().defaultGSon()
        val jsonData = gson.toJson(body)
        val requestAttributes = RequestContextHolder.getRequestAttributes() as ServletRequestAttributes
        requestAttributes.request.setAttribute(EnumRequestAttributes.REQUEST_BODY_DATA.name, jsonData)

        return body
    }
}
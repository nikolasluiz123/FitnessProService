package br.com.fitnesspro.config.advice

import br.com.fitnesspro.config.gson.defaultGSon
import br.com.fitnesspro.config.request.EnumRequestAttributes
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
        val gson = GsonBuilder().defaultGSon()
        val jsonData = gson.toJson(body)
        val requestAttributes = RequestContextHolder.getRequestAttributes() as ServletRequestAttributes
        requestAttributes.request.setAttribute(EnumRequestAttributes.RESPONSE_DATA.name, jsonData)

//        when (body) {
//            is AuthenticationServiceResponse -> {
//
//            }
//
//            is ExportationServiceResponse -> {
//
//            }
//
//            is FitnessProServiceResponse -> {
//
//            }
//
//            is ImportationServiceResponse<*> -> {
//
//            }
//
//            is PersistenceServiceResponse<*> -> {
//
//            }
//
//            is ReadServiceResponse<*> -> {
//
//            }
//
//            is SingleValueServiceResponse<*> -> {
//
//            }
//        }

        return body
    }
}
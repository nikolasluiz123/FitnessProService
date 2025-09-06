package br.com.fitnesspro.service.communication.extensions

import br.com.fitnesspro.core.exceptions.NotFoundException
import org.springframework.context.MessageSource
import java.util.*
import kotlin.jvm.optionals.getOrElse
import kotlin.reflect.KClass

fun <T : Any> Optional<T>.getOrThrowDefaultException(messageSource: MessageSource, clazz: KClass<T>): T {
    return getOrElse {
        throw NotFoundException(
            messageSource.getMessage(
                "core.entity.not.found",
                arrayOf(clazz.simpleName),
                Locale.getDefault()
            )
        )
    }
}
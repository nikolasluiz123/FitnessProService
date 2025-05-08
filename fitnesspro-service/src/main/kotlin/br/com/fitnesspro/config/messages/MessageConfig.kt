package br.com.fitnesspro.config.messages

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource

@Configuration
class MessageConfig {

    @Bean
    fun messageSource(): MessageSource {
        val messageSource = ReloadableResourceBundleMessageSource()
        messageSource.setBasenames(
            "classpath:messages-notification",
            "classpath:messages-custom-validation"
        )

        messageSource.setDefaultEncoding("UTF-8")

        return messageSource
    }
}
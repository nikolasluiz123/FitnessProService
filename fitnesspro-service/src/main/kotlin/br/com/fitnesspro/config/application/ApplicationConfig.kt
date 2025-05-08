package br.com.fitnesspro.config.application

import br.com.fitnesspro.config.interceptors.LoggingInterceptor
import br.com.fitnesspro.repository.general.user.IUserRepository
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.nio.charset.StandardCharsets

@Configuration
class ApplicationConfig(
    private val userRepository: IUserRepository,
    private val loggingInterceptor: LoggingInterceptor
): WebMvcConfigurer {

    @Bean
    fun userDetailsService(): UserDetailsService {
        return UserDetailsService { email ->
            userRepository.findByEmail(email) ?: throw UsernameNotFoundException("Usuário não encontrado")
        }
    }

    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val authenticationProvider = DaoAuthenticationProvider()
        authenticationProvider.setUserDetailsService(userDetailsService())
        authenticationProvider.setPasswordEncoder(passwordEncoder())

        return authenticationProvider
    }

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
        return config.authenticationManager
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return FitnessProPasswordEncoder()
    }

    override fun configureContentNegotiation(configurer: ContentNegotiationConfigurer) {
        configurer.defaultContentType(MediaType.APPLICATION_JSON_UTF8)
    }

    @Bean
    fun tomcatCustomizer(): WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
        return WebServerFactoryCustomizer { factory ->
            factory.addConnectorCustomizers({ connector ->
                connector?.uriEncoding = "UTF-8"
                connector?.setProperty("characterEncoding", "UTF-8")
                connector?.setProperty("useBodyEncodingForURI", "true")
            })
        }
    }

    @Bean
    fun stringHttpMessageConverter(): StringHttpMessageConverter {
        return StringHttpMessageConverter(StandardCharsets.UTF_8)
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(loggingInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns(
                "/health-check",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/api/v1/logs",
                "/api/v1/logs/**")
    }
}
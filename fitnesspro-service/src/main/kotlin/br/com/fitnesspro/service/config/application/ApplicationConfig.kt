package br.com.fitnesspro.service.config.application

import br.com.fitnesspro.service.config.interceptors.LoggingInterceptor
import br.com.fitnesspro.service.repository.general.user.IUserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


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

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(loggingInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns("/health-check", "/swagger-ui/**", "/v3/api-docs/**")
    }
}
package br.com.fitnesspro.service.config.application

import br.com.fitnesspro.service.config.application.customdetails.ApplicationUserDetails
import br.com.fitnesspro.service.config.application.customdetails.DeviceUserDetails
import br.com.fitnesspro.service.service.serviceauth.TokenService
import br.com.fitnesspro.shared.communication.enums.serviceauth.EnumErrorType
import br.com.fitnesspro.shared.communication.enums.serviceauth.EnumTokenType.*
import br.com.fitnesspro.shared.communication.exception.ExpiredTokenException
import br.com.fitnesspro.shared.communication.exception.NotFoundTokenException
import br.com.fitnesspro.shared.communication.responses.AuthenticationServiceResponse
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JWTAuthenticationFilter(
    private val service: TokenService,
    private val userDetailsService: UserDetailsService,
    private val objectMapper: ObjectMapper
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val authHeader: String? = request.getHeader("Authorization")

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw NotFoundTokenException()
            }

            val jwtToken = authHeader.substring(7)


            val serviceToken = service.getValidatedServiceToken(jwtToken)

            when (serviceToken.type!!) {
                USER_AUTHENTICATION_TOKEN -> {
                    val userDetails = userDetailsService.loadUserByUsername(serviceToken.user?.email!!)
                    authenticate(userDetails, request)
                }

                DEVICE_TOKEN -> {
                    val deviceDetails = DeviceUserDetails(serviceToken.device?.id!!)
                    authenticate(deviceDetails, request)
                }

                APPLICATION_TOKEN -> {
                    val applicationDetails = ApplicationUserDetails(serviceToken.application?.id!!)
                    authenticate(applicationDetails, request)
                }
            }

            filterChain.doFilter(request, response)
        } catch (ex: ExpiredTokenException) {
            val errorResponse = getAuthResponse(ex, EnumErrorType.EXPIRED_TOKEN)
            writeHTTPResponse(response, errorResponse)
        } catch (ex: NotFoundTokenException) {
            val errorResponse = getAuthResponse(ex, EnumErrorType.INVALID_TOKEN)
            writeHTTPResponse(response, errorResponse)
        }
    }

    private fun authenticate(userDetails: UserDetails, request: HttpServletRequest) {
        val authenticationToken = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
        authenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = authenticationToken
    }

    private fun writeHTTPResponse(response: HttpServletResponse, errorResponse: AuthenticationServiceResponse) {
        response.status = HttpStatus.UNAUTHORIZED.value()
        response.contentType = "application/json"
        response.writer.write(objectMapper.writeValueAsString(errorResponse))
    }

    private fun getAuthResponse(ex: Exception, type: EnumErrorType): AuthenticationServiceResponse {
        return AuthenticationServiceResponse(
            code = HttpStatus.UNAUTHORIZED.value(),
            success = false,
            error = ex.message,
            errorType = type
        )
    }
}
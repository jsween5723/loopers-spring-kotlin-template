package com.loopers.interfaces.api

import com.loopers.domain.auth.Authentication
import com.loopers.domain.user.UserId
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class AuthenticationFilter : OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val authorizationString = response.getHeader(AUTHORIZATION)
        if (authorizationString == null || authorizationString.isBlank()) {
            AuthenticationHolder.AUTHENTICATION_CONTEXT.set(Authentication(UserId(-1)))
        } else {
            val userId = try {
                authorizationString.toLong()
            } catch (e: NumberFormatException) {
                response.sendError(401)
                throw e
            }
            AuthenticationHolder.AUTHENTICATION_CONTEXT.set(Authentication(userId = UserId(userId)))
        }
        filterChain.doFilter(request, response)
    }
}

class AuthenticationHolder {
    companion object {
        val AUTHENTICATION_CONTEXT = ThreadLocal<Authentication>()
    }
}

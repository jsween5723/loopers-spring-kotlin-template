package com.loopers.interfaces.api.filter

import com.loopers.domain.auth.AnonymousAuthentication
import com.loopers.domain.auth.Authentication
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.UUID

@Component
class AuthenticationFilter : OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val authorizationString = request.getHeader(AUTHENTICATION_HEADER)
        if (authorizationString == null || authorizationString.isBlank()) {
            request.setAttribute(AUTHENTICATION_ATTRIBUTE, AnonymousAuthentication())
        } else {
            try {
                val userId = UUID.fromString(authorizationString)
                request.setAttribute(AUTHENTICATION_ATTRIBUTE, Authentication(userId))
            } catch (e: IllegalArgumentException) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
            }
        }
        filterChain.doFilter(request, response)
    }

    companion object {
        const val AUTHENTICATION_ATTRIBUTE = "authentication"
        const val AUTHENTICATION_HEADER = "X-USER-ID"
    }
}

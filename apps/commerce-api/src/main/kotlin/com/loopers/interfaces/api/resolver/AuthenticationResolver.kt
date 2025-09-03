package com.loopers.interfaces.api.resolver

import com.loopers.domain.auth.Authentication
import com.loopers.interfaces.api.filter.AuthenticationFilter.Companion.AUTHENTICATION_ATTRIBUTE
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class AuthenticationResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean =
        parameter.parameterType == Authentication::class.java

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Any = webRequest.getAttribute(AUTHENTICATION_ATTRIBUTE, 0) as Authentication
}

package com.loopers.interfaces.api

import com.loopers.domain.auth.Authentication
import com.loopers.domain.user.UserId
import jakarta.servlet.http.HttpServletRequest
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
    ): Any = runCatching {
        webRequest.getNativeRequest(HttpServletRequest::class.java)!!
            .getHeader("X-USER-ID")
            .let {
                Authentication(userId = UserId(it.toLong()))
            }
    }.getOrElse {
        throw IllegalArgumentException("X-USER-ID가 비어있습니다.")
    }
}

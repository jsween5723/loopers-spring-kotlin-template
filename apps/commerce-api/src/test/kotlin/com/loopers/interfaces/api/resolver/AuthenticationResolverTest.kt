package com.loopers.interfaces.api.resolver

import com.loopers.domain.auth.Authentication
import com.loopers.interfaces.api.filter.AuthenticationFilter
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.core.MethodParameter
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.web.context.request.ServletWebRequest
import java.util.UUID

class AuthenticationResolverTest {
    private val sut = AuthenticationResolver()

    class TestController {
        fun testMethod(authentication: Authentication){

        }
        fun testMethod(authentication: Long){}
    }

    @Test
    fun `request에 세팅된 Authentication이 반환된다`() {
        //given
        val request = MockHttpServletRequest()
        val authentication = Authentication(userId = UUID.randomUUID())
        request.setAttribute(AuthenticationFilter.Companion.AUTHENTICATION_ATTRIBUTE, authentication)
        val method = TestController::class.java.getDeclaredMethod("testMethod", Authentication::class.java)
        val parameter = MethodParameter(method, 0)
        //when
        val actual = sut.resolveArgument(parameter, null, ServletWebRequest(request), null)
        //then
        Assertions.assertThat(actual)
            .isEqualTo(authentication)
    }

    @Test
    fun `파라미터 타입이 다르면 동작하지 않는다`() {
        //given
        val request = MockHttpServletRequest()
        val authentication = Authentication(userId = UUID.randomUUID())
        request.setAttribute(AuthenticationFilter.Companion.AUTHENTICATION_ATTRIBUTE, authentication)
        val method = TestController::class.java.getDeclaredMethod("testMethod", Long::class.java)
        val parameter = MethodParameter(method, 0)
        //when
        val actual = sut.supportsParameter(parameter)
        //then
        Assertions.assertThat(actual)
            .isFalse()
    }

    @Test
    fun `파라미터 타입이 Authentication이면 동작한다`() {
        //given
        val request = MockHttpServletRequest()
        val authentication = Authentication(userId = UUID.randomUUID())
        request.setAttribute(AuthenticationFilter.Companion.AUTHENTICATION_ATTRIBUTE, authentication)
        val method = TestController::class.java.getDeclaredMethod("testMethod", Authentication::class.java)
        val parameter = MethodParameter(method, 0)
        //when
        val actual = sut.supportsParameter(parameter)
        //then
        Assertions.assertThat(actual)
            .isTrue()
    }

}

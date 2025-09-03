package com.loopers.interfaces.api.filter

import com.loopers.domain.auth.Authentication
import com.loopers.domain.auth.Role
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockFilterChain
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import java.util.UUID

class AuthenticationFilterTest {
    private val sut = AuthenticationFilter()

    @Test
    fun `AUTHORIZATION_HEADER에 값이 없으면 AnonymousAuthentication이 주입된다`() {
        //given
        val request = MockHttpServletRequest()
        request.addHeader(AuthenticationFilter.Companion.AUTHENTICATION_HEADER, "")
        val response = MockHttpServletResponse()
        val filterChain = MockFilterChain()
        //when
        sut.doFilter(request, response, filterChain)
        val authentication = request.getAttribute(AuthenticationFilter.Companion.AUTHENTICATION_ATTRIBUTE) as Authentication
        //then
        Assertions.assertThat(authentication.roles)
            .contains(Role.ANONYMOUS)
    }

    @Test
    fun `AUTHORIZATION_HEADER에 값이 불량이면 401 에러코드가 반환된다`() {
        //given
        val request = MockHttpServletRequest()
        request.addHeader(AuthenticationFilter.Companion.AUTHENTICATION_HEADER, "123")
        val response = MockHttpServletResponse()
        val filterChain = MockFilterChain()
        //when
        sut.doFilter(request, response, filterChain)
        //then
        Assertions.assertThat(response.status)
            .isEqualTo(401)
    }

    @Test
    fun `AUTHORIZATION_HEADER에 값이 UUID면 USER 롤을 가진 Authentication이 반환된다`() {
        //given
        val request = MockHttpServletRequest()
        request.addHeader(
            AuthenticationFilter.Companion.AUTHENTICATION_HEADER,
            UUID.randomUUID()
                .toString(),
        )
        val response = MockHttpServletResponse()
        val filterChain = MockFilterChain()
        //when
        sut.doFilter(request, response, filterChain)
        val authentication = request.getAttribute(AuthenticationFilter.Companion.AUTHENTICATION_ATTRIBUTE) as Authentication
        //then
        Assertions.assertThat(authentication.roles)
            .contains(Role.USER)
    }
}

package com.loopers.domain.auth

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class AuthenticationTest {
    @Test
    fun `hasRole에 있는 권한이 roles에 없으면 IllegalAccessException이 발생한다`() {
        val anonymous = AnonymousAuthentication()
        assertThatThrownBy { anonymous.hasRole(Role.USER){} }
            .isInstanceOf(IllegalAccessException::class.java)
    }
}

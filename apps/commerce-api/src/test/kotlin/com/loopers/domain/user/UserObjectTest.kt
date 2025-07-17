package com.loopers.domain.user

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("사용자 도메인 Object 클래스 인스턴스 생성테스트")
class UserObjectTest {
    @Test
    fun `UserCommand`() {
        Class.forName("com.loopers.domain.user.UserCommand")
    }
}

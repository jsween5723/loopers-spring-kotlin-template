package com.loopers.interfaces.api.v1.points

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("사용자 E2E Object 클래스 인스턴스 생성테스트")
class UserPointObjectTest {
    @Test
    fun `UserCommand`() {
        Class.forName("com.loopers.interfaces.api.v1.points.UserPointRequest")
        Class.forName("com.loopers.interfaces.api.v1.points.UserPointResponse")
    }
}

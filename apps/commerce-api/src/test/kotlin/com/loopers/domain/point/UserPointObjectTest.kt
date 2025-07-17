package com.loopers.domain.point

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("포인트 도메인 Object 클래스 인스턴스 생성테스트")
class UserPointObjectTest {
    @Test
    fun `UserPointCommand`() {
        Class.forName("com.loopers.domain.point.UserPointCommand")
    }
}

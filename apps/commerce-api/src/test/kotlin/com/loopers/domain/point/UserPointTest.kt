package com.loopers.domain.point

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@DisplayName("포인트 단위테스트")
class UserPointTest {
    private lateinit var userPoint: UserPoint
    private val wrongChargeAmount = (-1L).toBigDecimal()

    @BeforeEach
    fun setUp() {
        userPoint = UserPoint(userId = 1L)
    }

    @Test
    fun `0 이하의 정수로 포인트를 충전 시 실패한다`() {
        // arrange
        // act
        // assert
        assertThrows<IllegalArgumentException> { userPoint.charge(wrongChargeAmount) }
    }
}

package com.loopers.domain.point

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

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
        assertThrows<IllegalArgumentException> { userPoint.charge(wrongChargeAmount) }
    }
}

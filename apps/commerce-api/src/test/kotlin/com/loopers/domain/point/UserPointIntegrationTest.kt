package com.loopers.domain.point

import com.loopers.application.point.UserPointFacade
import com.loopers.domain.IntegrationTest
import com.loopers.domain.IntegrationTestFixture
import com.loopers.domain.IntegrationTestFixture.Companion.NO_EXIST_USER_ID
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.assertEquals

@IntegrationTest
class UserPointIntegrationTest {
    @Autowired
    private lateinit var userPointFacade: UserPointFacade

    @Test
    fun `존재하지 않는 유저 ID 로 충전을 시도한 경우, 실패한다`(@Autowired fixture: IntegrationTestFixture) {
        // arrange
        val command = UserPointCommandGenerator.Charge(userId = NO_EXIST_USER_ID)
        // act
        assertThrows<NoSuchElementException> { userPointFacade.charge(command) }
    }

    @Test
    fun `조회 시 해당 ID 의 회원이 존재할 경우, 보유 포인트가 반환된다`(@Autowired fixture: IntegrationTestFixture) {
        // arrange
        val 사용자 = fixture.기본_사용자_등록()
        val 충전된_포인트 = fixture.충전하기()
        // act
        val myPoint = userPointFacade.getMe(userId = 사용자.id)
        // assert
        assertEquals(
            0,
            충전된_포인트.point
        .compareTo(requireNotNull(myPoint?.point)),
        )
    }
}

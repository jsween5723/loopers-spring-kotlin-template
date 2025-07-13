package com.loopers.domain.point

import com.loopers.application.point.UserPointFacade
import com.loopers.domain.IntegrationTest
import com.loopers.domain.IntegrationTestFixture
import com.loopers.domain.IntegrationTestFixture.Companion.NO_EXIST_USER_ID
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired

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
}

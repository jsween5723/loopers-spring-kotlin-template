package com.loopers.domain.point

import com.loopers.application.point.UserPointFacade
import com.loopers.domain.AbstractIntegrationTest
import com.loopers.domain.IntegrationTestFixture
import com.loopers.domain.IntegrationTestFixture.Companion.NO_EXIST_USER_ID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertNull

@DisplayName("포인트 통합테스트")
class UserPointIntegrationTest(
    private val userPointFacade: UserPointFacade,
    private val fixture: IntegrationTestFixture,
) : AbstractIntegrationTest() {

    @Nested
    inner class `포인트 충전` {
        @Test
        fun `존재하지 않는 유저 ID 로 충전을 시도한 경우, 실패한다`() {
            // arrange
            val command = UserPointCommandGenerator.Charge(userId = NO_EXIST_USER_ID)
            // act
            assertThrows<NoSuchElementException> { userPointFacade.charge(command) }
        }
    }

    @Nested
    inner class `포인트 조회` {
        @Test
        fun `조회 시 해당 ID 의 회원이 존재할 경우, 보유 포인트가 반환된다`() {
            // arrange
            val 사용자 = fixture.기본_사용자_등록()
            val 충전된_포인트 = fixture.충전하기()
            // act
            val myPoint = userPointFacade.getMe(userId = 사용자.id)
            // assert
            assertThat(requireNotNull(myPoint).point)
                .isEqualByComparingTo(충전된_포인트.point)
        }

        @Test
        fun `해당 ID 의 회원이 존재하지 않을 경우, null 이 반환된다`() {
            // arrange
            // act
            val result = userPointFacade.getMe(NO_EXIST_USER_ID)
            // assert
            assertNull(result)
        }
    }
}

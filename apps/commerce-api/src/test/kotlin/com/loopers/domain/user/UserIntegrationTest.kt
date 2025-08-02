package com.loopers.domain.user

import com.loopers.domain.AbstractIntegrationTest
import com.loopers.domain.IntegrationTestFixture
import com.loopers.domain.IntegrationTestFixture.Companion.NO_EXIST_USER_ID
import com.ninjasquad.springmockk.SpykBean
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertNull

@DisplayName("사용자 통합테스트")
class UserIntegrationTest(
    @SpykBean private val repository: UserRepository,
    private val fixture: IntegrationTestFixture,
) : AbstractIntegrationTest() {

    @Nested
    inner class `사용자 생성` {
        @Test
        fun `회원 가입시 User 저장이 수행된다`() {
            // arrange
            val command = UserCommandGenerator.Create()
            // act
            val user = fixture.userService.create(command)
            // assert
            verify { repository.save(any()) }
            assertThat(user.id).isNotEqualTo(0)
            assertThat(user).usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt", "id", "deletedAt")
                .isEqualTo(command)
        }

        @Test
        fun `이미 가입된 ID 로 회원가입 시도 시, IllegalStateException가 발생한다`() {
            // arrange
            val 가입된_사용자 = fixture.회원가입()
            val command = UserCommandGenerator.Create(username = 가입된_사용자.username)
            // act
            // assert
            assertThatThrownBy { fixture.userService.create(command) }
                .isInstanceOf(IllegalStateException::class.java)
                .hasMessageContaining(command.username)
        }
    }

    @Nested
    inner class `내 정보 조회` {
        @Test
        fun `해당 ID 의 회원이 존재할 경우, 회원 정보가 반환된다`() {
            // arrange
            val existUser = fixture.회원가입()
            // act
            val result = fixture.userService.read(UserId(existUser.id))
            // assert
            assertThat(requireNotNull(result)).usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt")
                .isEqualTo(existUser)
        }

        @Test
        fun `해당 ID 의 회원이 존재하지 않을 경우, null 이 반환된다`() {
            // arrange
            // act
            val result = fixture.userService.read(UserId(NO_EXIST_USER_ID))
            // assert
            assertNull(result)
        }
    }
}

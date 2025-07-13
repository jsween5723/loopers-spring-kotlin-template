package com.loopers.domain.user

import com.loopers.domain.IntegrationTest
import com.ninjasquad.springmockk.SpykBean
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import kotlin.test.assertNotNull

@IntegrationTest
class UserIntegrationTest {
    @SpykBean
    private lateinit var repository: UserRepository

    @Test
    fun `회원 가입시 User 저장이 수행된다`(@Autowired fixture: UserIntegrationTestFixture) {
        // arrange
        val command = UserCommandGenerator.Create()
        // act
        val user = fixture.userService.create(command)
        // assert
        verify { repository.save(any()) }
        assertNotNull(user.id)
    }

    @Test
    fun `이미 가입된 ID 로 회원가입 시도 시, 실패한다`(@Autowired fixture: UserIntegrationTestFixture) {
        // arrange
        val 가입된_사용자 = fixture.회원가입()
        // act
        // assert
        assertThrows<DataIntegrityViolationException> { fixture.userService.create(UserCommandGenerator.Create(username = 가입된_사용자.username)) }
    }
}

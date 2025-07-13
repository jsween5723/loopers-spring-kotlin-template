package com.loopers.domain.user

import com.loopers.application.user.UserService
import com.loopers.domain.IntegrationTest
import com.ninjasquad.springmockk.SpykBean
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import kotlin.test.assertNotNull

@IntegrationTest
class UserIntegrationTest {
    @SpykBean
    private lateinit var repository: UserRepository

    @Autowired
    private lateinit var userService: UserService

    @Test
    fun `회원 가입시 User 저장이 수행된다`() {
        // arrange
        val command = UserCommandGenerator.Create()
        // act
        val user = userService.create(command)
        // assert
        verify { repository.save(any()) }
        assertNotNull(user.id)
    }
}

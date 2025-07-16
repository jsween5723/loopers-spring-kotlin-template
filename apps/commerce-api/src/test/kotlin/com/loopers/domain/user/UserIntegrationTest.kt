package com.loopers.domain.user

import com.loopers.domain.AbstractIntegrationTest
import com.loopers.domain.IntegrationTestFixture
import com.loopers.domain.IntegrationTestFixture.Companion.NO_EXIST_USER_ID
import com.ninjasquad.springmockk.SpykBean
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class UserIntegrationTest : AbstractIntegrationTest() {
    @SpykBean
    private lateinit var repository: UserRepository

    @Test
    fun `회원 가입시 User 저장이 수행된다`(@Autowired fixture: IntegrationTestFixture) {
        // arrange
        val command = UserCommandGenerator.Create()
        // act
        val user = fixture.userService.create(command)
        // assert
        verify { repository.save(any()) }
        assertNotNull(user.id)
    }

    @Test
    fun `이미 가입된 ID 로 회원가입 시도 시, 실패한다`(@Autowired fixture: IntegrationTestFixture) {
        // arrange
        val 가입된_사용자 = fixture.회원가입()
        // act
        // assert
        assertThrows<DataIntegrityViolationException> { fixture.userService.create(UserCommandGenerator.Create(username = 가입된_사용자.username)) }
    }

    @Test
    fun `해당 ID 의 회원이 존재할 경우, 회원 정보가 반환된다`(@Autowired fixture: IntegrationTestFixture) {
        // arrange
        val existUser = fixture.회원가입()
        // act
        val result = fixture.userService.read(existUser.id)
        // assert
        assertNotNull(result)
        assertEquals(existUser.name, result.name)
        assertEquals(existUser.username, result.username)
        assertEquals(existUser.id, result.id)
        assertEquals(existUser.gender, result.gender)
        assertEquals(existUser.email, result.email)
        assertEquals(existUser.birth, result.birth)
    }

    @Test
    fun `해당 ID 의 회원이 존재하지 않을 경우, null 이 반환된다`(@Autowired fixture: IntegrationTestFixture) {
        // arrange
        // act
        val result = fixture.userService.read(NO_EXIST_USER_ID)
        // assert
        assertNull(result)
    }
}

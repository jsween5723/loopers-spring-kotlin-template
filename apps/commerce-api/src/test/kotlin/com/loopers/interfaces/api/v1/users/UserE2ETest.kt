package com.loopers.interfaces.api.v1.users

import com.loopers.interfaces.api.v1.ApiTest
import com.loopers.interfaces.api.v1.users.UserE2ETestFixture.Companion.CREATE_URI
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.postForEntity

@ApiTest
class UserE2ETest {
    @Test
    fun `회원 가입이 성공할 경우, 생성된 유저 정보를 응답으로 반환한다`(@Autowired userE2ETestFixture: UserE2ETestFixture) {
        // arrange
        val create = UserRequestGenerator.Create()
        // act
        val response = userE2ETestFixture.testRestTemplate.postForEntity<UserResponse.Create>(
            CREATE_URI,
            create,
        )
        // assert
        assertEquals(200, response.statusCode.value())
        assertNotNull(response.body)
        assertNotNull(response.body?.id)
        assertEquals(create.name, requireNotNull(response.body?.name))
        assertEquals(create.email, requireNotNull(response.body?.email))
        assertEquals(create.gender, requireNotNull(response.body?.gender))
        assertEquals(create.birth, requireNotNull(response.body?.birth))
    }
}

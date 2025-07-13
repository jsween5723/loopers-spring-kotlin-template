package com.loopers.interfaces.api.v1.users

import com.loopers.interfaces.api.v1.ApiTest
import com.loopers.interfaces.api.v1.ApiTestFixture
import com.loopers.interfaces.api.v1.ApiTestFixture.Companion.USER_CREATE_URI
import com.loopers.interfaces.api.v1.ApiTestFixture.Companion.USER_GET_ME_URI
import com.loopers.interfaces.api.v1.ApiTestFixture.Companion.UNAVAILABLE_USER_ID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ProblemDetail

@ApiTest
class UserE2ETest {
    @Test
    fun `회원 가입이 성공할 경우, 생성된 유저 정보를 응답으로 반환한다`(@Autowired fixture: ApiTestFixture) {
        // arrange
        val create = UserRequestGenerator.Create()
        // act
        val response = fixture.testRestTemplate.postForEntity<UserResponse.Create>(
            USER_CREATE_URI,
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

    @Test
    fun `회원 가입 시에 성별이 없을 경우, 400 Bad Request 응답을 반환한다`(@Autowired fixture: ApiTestFixture) {
        // arrange
        val create = UserRequestGenerator.Create(gender = "")
        // act
        val response = fixture.testRestTemplate.postForEntity<ProblemDetail>(
            USER_CREATE_URI,
            create,
        )
        // assert
        assertEquals(400, response.statusCode.value())
    }

    @Test
    fun `내 정보 조회에 성공할 경우, 해당하는 유저 정보를 응답으로 반환한다`(@Autowired fixture: ApiTestFixture) {
        // arrange
        val 사용자 = fixture.회원가입()
        fixture.사용자_지정(사용자.id)
        // act
        val result = fixture.testRestTemplate.exchange(
            USER_GET_ME_URI,
            HttpMethod.GET,
            HttpEntity.EMPTY,
            UserResponse.GetMe::class.java,
        )
        // assert
        assertEquals(200, result.statusCode.value())
        assertNotNull(result.body)
        assertEquals(사용자.id, requireNotNull(result.body?.id))
        assertEquals(사용자.name, requireNotNull(result.body?.name))
        assertEquals(사용자.username, requireNotNull(result.body?.username))
        assertEquals(사용자.email, requireNotNull(result.body?.email))
        assertEquals(사용자.gender, requireNotNull(result.body?.gender))
        assertEquals(사용자.birth, requireNotNull(result.body?.birth))
    }

    @Test
    fun `존재하지 않는 ID 로 조회할 경우, 404 Not Found 응답을 반환한다`(@Autowired fixture: ApiTestFixture) {
        // arrange
        fixture.사용자_지정(UNAVAILABLE_USER_ID)
        // act
        val result = fixture.testRestTemplate.exchange(
            USER_GET_ME_URI,
            HttpMethod.GET,
            HttpEntity.EMPTY,
            ProblemDetail::class.java,
        )
        // assert
        assertEquals(404, result.statusCode.value())
    }
}

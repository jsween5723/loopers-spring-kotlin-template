package com.loopers.interfaces.api.v1.users

import com.loopers.interfaces.api.AbstractApiTest
import com.loopers.interfaces.api.v1.ApiTestFixture
import com.loopers.interfaces.api.v1.ApiTestFixture.Companion.NOT_EXIST_USER_ID
import com.loopers.interfaces.api.v1.ApiTestFixture.Companion.USER_CREATE_URI
import com.loopers.interfaces.api.v1.ApiTestFixture.Companion.USER_GET_ME_URI
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.boot.test.web.client.exchange
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail

@DisplayName("사용자 E2E 테스트")
class UserE2ETest(private val fixture: ApiTestFixture) : AbstractApiTest() {

    @Nested
    inner class `POST api_v1_users` {
        @Test
        fun `회원 가입이 성공할 경우, 생성된 유저 정보를 응답으로 반환한다`() {
            // arrange
            val create = UserRequestGenerator.Create()
            // act
            val response = fixture.testRestTemplate.postForEntity<UserResponse.Create>(
                USER_CREATE_URI,
                create,
            )
            // assert
            assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
            assertThat(requireNotNull(response.body)).usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt", "id")
                .isEqualTo(create)
        }

        @Test
        fun `회원 가입 시에 성별이 없을 경우, 400 Bad Request 응답을 반환한다`() {
            // arrange
            val create = UserRequestGenerator.Create(gender = "")
            // act
            val response = fixture.testRestTemplate.postForEntity<ProblemDetail>(
                USER_CREATE_URI,
                create,
            )
            // assert
            assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        }
    }

    @Nested
    inner class `GET api_v1_users_me` {
        @Test
        fun `내 정보 조회에 성공할 경우, 해당하는 유저 정보를 응답으로 반환한다`() {
            // arrange
            val 사용자 = fixture.회원가입()
            fixture.사용자_지정(사용자.id)
            // act
            val result = fixture.testRestTemplate.exchange<UserResponse.GetMe>(
                USER_GET_ME_URI,
                HttpMethod.GET,
                HttpEntity.EMPTY,
            )
            // assert
            assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
            assertThat(requireNotNull(result.body)).usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt")
                .isEqualTo(사용자)
        }

        @Test
        fun `존재하지 않는 ID 로 조회할 경우, 404 Not Found 응답을 반환한다`() {
            // arrange
            fixture.사용자_지정(NOT_EXIST_USER_ID)
            // act
            val result = fixture.testRestTemplate.exchange<ProblemDetail>(
                USER_GET_ME_URI,
                HttpMethod.GET,
                HttpEntity.EMPTY,
            )
            // assert
            assertThat(result.statusCode.value()).isEqualTo(404)
        }
    }
}

package com.loopers.interfaces.api.v1.points

import com.loopers.interfaces.api.AbstractApiTest
import com.loopers.interfaces.api.v1.ApiTestFixture
import com.loopers.interfaces.api.v1.ApiTestFixture.Companion.GET_MY_POINT_URI
import com.loopers.interfaces.api.v1.ApiTestFixture.Companion.NOT_EXIST_USER_ID
import com.loopers.interfaces.api.v1.ApiTestFixture.Companion.POINT_CHARGE_URI
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.exchange
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail

@DisplayName("포인트 E2E 테스트")
class UserPointPayE2ETest(private val fixture: ApiTestFixture) : AbstractApiTest() {
    @Nested
    inner class `GET api_v1_points` {

        @Test
        fun `조회 시 X-USER-ID 헤더가 없을 경우, 404 NOT_FOUND 응답을 반환한다`() {
            // arrange
            // act
            val result = fixture.testRestTemplate.exchange<ProblemDetail>(
                GET_MY_POINT_URI,
                HttpMethod.GET,
                HttpEntity.EMPTY,
            )
            // assert
            assertThat(requireNotNull(result.statusCode))
                .isEqualTo(HttpStatus.NOT_FOUND)
        }

        @Test
        fun `포인트 조회에 성공할 경우, 보유 포인트를 응답으로 반환한다`() {
            // arrange
            fixture.기본_사용자_지정()
            val 기존_포인트 = fixture.포인트_충전()
            // act
            val result = fixture.testRestTemplate.exchange<UserPointResponse.GetMine>(
                GET_MY_POINT_URI,
                HttpMethod.GET,
                HttpEntity.EMPTY,
            )
            // assert
            assertThat(requireNotNull(result.statusCode))
                .isEqualTo(HttpStatus.OK)
            assertThat(requireNotNull(result.body).point)
                .isEqualByComparingTo(기존_포인트.point)
        }

        @Test
        fun `포인트 조회시 유효한 사용자 ID가 아닐경우, 404 Not Found를 반환한다`() {
            // arrange
            fixture.사용자_지정(NOT_EXIST_USER_ID)
            // act
            val result = fixture.testRestTemplate.exchange<ProblemDetail>(
                GET_MY_POINT_URI,
                HttpMethod.GET,
                HttpEntity.EMPTY,
            )
            // assert
            assertThat(requireNotNull(result.statusCode))
                .isEqualTo(HttpStatus.NOT_FOUND)
        }
    }

    @Nested
    inner class `POST api_v1_points_charge` {
        @Test
        fun `존재하는 유저가 1000원을 충전할 경우, 충전된 보유 총량을 응답으로 반환한다`(
            @Autowired fixture: ApiTestFixture,
        ) {
            // arrange
            fixture.기본_사용자_지정()
            val request = UserPointRequestGenerator.Charge(amount = 1000.toBigDecimal())
            // act
            val result = fixture.testRestTemplate.postForEntity<UserPointResponse.Charge>(
                POINT_CHARGE_URI,
                request,
            )
            // assert
            assertThat(requireNotNull(result.body).point)
                .isEqualByComparingTo(1000.toBigDecimal())
        }

        @Test
        fun `존재하지 않는 유저로 충전할 경우, 404 Not Found 응답을 반환한다`() {
            // arrange
            fixture.사용자_지정(NOT_EXIST_USER_ID)
            val request = UserPointRequestGenerator.Charge()
            // act
            val result =
                fixture.testRestTemplate.postForEntity<ProblemDetail>(POINT_CHARGE_URI, request)
            // assert
            assertThat(result.statusCode)
                .isEqualTo(HttpStatus.NOT_FOUND)
        }
    }
}

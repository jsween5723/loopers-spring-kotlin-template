package com.loopers.interfaces.api.v1.points

import com.loopers.interfaces.api.v1.ApiTest
import com.loopers.interfaces.api.v1.ApiTestFixture
import com.loopers.interfaces.api.v1.ApiTestFixture.Companion.POINT_CHARGE_URI
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.patchForObject
import org.springframework.http.ProblemDetail
import kotlin.test.assertEquals

@ApiTest
class UserPointE2ETest {
    @Test
    fun `존재하는 유저가 1000원을 충전할 경우, 충전된 보유 총량을 응답으로 반환한다`(@Autowired fixture: ApiTestFixture) {
        // arrange
        fixture.기본_사용자_지정()
        val request = UserPointRequestGenerator.Charge(amount = 1000.toBigDecimal())
        // act
        val result = fixture.testRestTemplate.patchForObject<UserPointResponse.Charge>(POINT_CHARGE_URI, request)
        // arrange
        assertEquals(0, 1000.toBigDecimal().compareTo(requireNotNull(result?.point)))
    }

    @Test
    fun `X-USER-ID 헤더가 없을 경우, 400 Bad Request 응답을 반환한다`(@Autowired fixture: ApiTestFixture) {
        // arrange
        val request = UserPointRequestGenerator.Charge(amount = 400.toBigDecimal())
        // act
        val result = fixture.testRestTemplate.patchForObject<ProblemDetail>(POINT_CHARGE_URI, request)
        // assert
        assertEquals(400, requireNotNull(result?.status))
    }
}

package com.loopers.interfaces.api.v1.points

import com.loopers.interfaces.api.v1.ApiTest
import com.loopers.interfaces.api.v1.ApiTestFixture
import com.loopers.interfaces.api.v1.ApiTestFixture.Companion.POINT_CHARGE_URI
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.patchForObject
import java.math.BigDecimal
import kotlin.test.assertEquals

@ApiTest
class PointE2ETest {
    @Test
    fun `존재하는 유저가 1000원을 충전할 경우, 충전된 보유 총량을 응답으로 반환한다`(@Autowired fixture: ApiTestFixture) {
        // arrange
        val 사용자 = fixture.기본_사용자_지정()
        val request = PointRequestGenerator.Charge(point = 1000.toBigDecimal())
        // act
        val result = fixture.testRestTemplate.patchForObject<PointResponse.Charge>(POINT_CHARGE_URI, request)
        // arrange
        assertEquals(BigDecimal.valueOf(1000L), requireNotNull(result?.point))
    }
}

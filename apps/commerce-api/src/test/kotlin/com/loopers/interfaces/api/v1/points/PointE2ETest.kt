package com.loopers.interfaces.api.v1.points

import com.loopers.interfaces.api.v1.ApiTest
import com.loopers.interfaces.api.v1.ApiTestFixture
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import kotlin.test.assertEquals

@ApiTest
class PointE2ETest {
    @Test
    fun `존재하는 유저가 1000원을 충전할 경우, 충전된 보유 총량을 응답으로 반환한다`(@Autowired fixture: ApiTestFixture) {
        // arrange
        val 사용자 = fixture.기본_사용자_지정()
        // act
        val result = fixture.testRestTemplate.postForEntity<PointResponse.Charge>(CHARGE_URI, request)
        // arrange
        assertEquals(200, result.statusCode.value())
        assertEquals(BigDecimal.valueOf(1000L), result.point)
    }
}

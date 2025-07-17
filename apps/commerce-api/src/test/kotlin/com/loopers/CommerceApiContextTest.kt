package com.loopers

import com.loopers.domain.IntegrationTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@IntegrationTest
@DisplayName("컨텍스트 로딩 테스트")
class CommerceApiContextTest {
    @Test
    fun contextLoads() {
        // 이 테스트는 Spring Boot 애플리케이션 컨텍스트가 로드되는지 확인합니다.
        // 모든 빈이 올바르게 로드되었는지 확인하는 데 사용됩니다.
    }
}

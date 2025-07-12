package com.loopers.interfaces.api.v1

import com.loopers.interfaces.api.v1.users.UserE2ETestFixture
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Bean

@TestConfiguration
class ApiTestConfiguration {
    @Bean
    fun userTestFixture(
        testRestTemplate: TestRestTemplate,
    ) = UserE2ETestFixture(testRestTemplate)
}

package com.loopers.domain

import com.loopers.application.user.UserService
import com.loopers.domain.user.UserIntegrationTestFixture
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class IntegrationTestConfiguration {
    @Bean
    fun userIntegrationTestFixture(userService: UserService): UserIntegrationTestFixture = UserIntegrationTestFixture(userService)
}

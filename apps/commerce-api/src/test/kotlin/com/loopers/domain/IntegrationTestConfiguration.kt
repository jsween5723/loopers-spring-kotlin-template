package com.loopers.domain

import com.loopers.application.user.UserService
import com.loopers.domain.point.UserPointService
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Scope

@TestConfiguration
class IntegrationTestConfiguration {
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    fun integrationTestFixture(
        userService: UserService,
        userPointService: UserPointService,
    ): IntegrationTestFixture = IntegrationTestFixture(userService, userPointService)
}

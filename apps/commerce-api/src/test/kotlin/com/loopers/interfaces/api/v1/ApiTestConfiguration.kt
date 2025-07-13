package com.loopers.interfaces.api.v1

import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Scope
import org.springframework.core.env.Environment

@TestConfiguration
class ApiTestConfiguration {
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    fun userTestFixture(
        environment: Environment,
    ) = ApiTestFixture(environment)
}

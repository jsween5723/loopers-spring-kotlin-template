package com.loopers.interfaces.api.v1

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(ApiTestConfiguration::class)
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiTest

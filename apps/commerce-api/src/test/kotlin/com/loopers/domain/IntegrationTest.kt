package com.loopers.domain

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.TestConstructor

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(IntegrationTestConfiguration::class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Retention(AnnotationRetention.RUNTIME)
annotation class IntegrationTest

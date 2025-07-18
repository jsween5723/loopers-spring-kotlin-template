 package com.loopers.interfaces.api

 import com.loopers.interfaces.api.v1.ApiTest
 import com.loopers.interfaces.api.v1.ApiTestFixture
 import com.ninjasquad.springmockk.SpykBean
 import io.mockk.every
 import org.junit.jupiter.api.Assertions.assertEquals
 import org.junit.jupiter.api.Assertions.assertNotNull
 import org.junit.jupiter.api.DisplayName
 import org.junit.jupiter.api.Test
 import org.springframework.boot.test.web.client.exchange
 import org.springframework.http.HttpEntity
 import org.springframework.http.HttpMethod
 import org.springframework.http.HttpStatus
 import org.springframework.http.ProblemDetail
 import org.springframework.web.server.ServerWebInputException

 @ApiTest
 @DisplayName("Advice 테스트")
 class ApiControllerAdviceTest(
    private val fixture: ApiTestFixture,
    @SpykBean private val healthCheckController: HealthCheckController,
 ) {
    @Test
    fun `INTERNAL_SERVER_ERROR`() {
        // arrange
        every { healthCheckController.healthCheck() }.throws(Throwable("INTERNAL_SERVER_ERROR"))
        // act
        val result = fixture.testRestTemplate.exchange<ProblemDetail>("/api", HttpMethod.GET, HttpEntity.EMPTY)
        // assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.statusCode)
        assertNotNull(result.body)
    }

    @Test
    fun `BAD_REQUEST ServerWebInputException`() {
        // arrange
        every { healthCheckController.healthCheck() }.throws(ServerWebInputException("BAD_REQUEST"))
        // act
        val result = fixture.testRestTemplate.exchange<ProblemDetail>("/api", HttpMethod.GET, HttpEntity.EMPTY)
        // assert
        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
        assertNotNull(result.body)
    }
 }

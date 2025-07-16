package com.loopers.interfaces.api.v1

import com.loopers.interfaces.api.v1.points.UserPointRequest
import com.loopers.interfaces.api.v1.points.UserPointRequestGenerator
import com.loopers.interfaces.api.v1.points.UserPointResponse
import com.loopers.interfaces.api.v1.users.UserRequest
import com.loopers.interfaces.api.v1.users.UserRequestGenerator
import com.loopers.interfaces.api.v1.users.UserResponse
import org.springframework.boot.test.web.client.LocalHostUriTemplateHandler
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.core.env.Environment

class ApiTestFixture(environment: Environment) {
    val testRestTemplate = TestRestTemplate(RestTemplateBuilder()).also {
        it.setUriTemplateHandler(LocalHostUriTemplateHandler(environment))
    }

    fun 사용자_지정(userId: Long) {
        testRestTemplate.restTemplate.interceptors.addFirst { request, body, execution ->
            request.headers.set("X-USER-ID", userId.toString())
            execution.execute(request, body)
        }
    }

    fun 기본_사용자_지정(): UserResponse.Create = 회원가입().also { user ->
        testRestTemplate.restTemplate.interceptors.addFirst { request, body, execution ->
            request.headers.set("X-USER-ID", user.id.toString())
            execution.execute(request, body)
        }
    }

    fun 회원가입(request: UserRequest.Create = UserRequestGenerator.Create()): UserResponse.Create {
        val response = testRestTemplate.postForEntity<UserResponse.Create>(
            USER_CREATE_URI,
            request,
        )
        return response.body!!
    }

    fun 포인트_충전(request: UserPointRequest.Charge = UserPointRequestGenerator.Charge()): UserPointResponse.Charge? =
        testRestTemplate.postForEntity<UserPointResponse.Charge>(
            POINT_CHARGE_URI,
            request,
        ).body

    companion object {
        const val USER_CREATE_URI = "/api/v1/users"
        const val USER_GET_ME_URI = "/api/v1/users/me"
        const val POINT_CHARGE_URI = "/api/v1/points/charge"
        const val GET_MY_POINT_URI = "/api/v1/points"
        const val UNAVAILABLE_USER_ID = -1L
    }
}

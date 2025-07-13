package com.loopers.interfaces.api.v1

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

    fun 기본_사용자_지정() {
        val 사용자 = 회원가입()
        testRestTemplate.restTemplate.interceptors.addFirst { request, body, execution ->
            request.headers.set("X-USER-ID", 사용자.id.toString())
            execution.execute(request, body)
        }
    }
    fun 회원가입(request: UserRequest.Create = UserRequestGenerator.Create()): UserResponse.Create {
        val response = testRestTemplate.postForEntity<UserResponse.Create>(
            CREATE_URI,
            request,
        )
        return response.body!!
    }

    companion object {
        const val CREATE_URI = "/api/v1/users"
        const val GET_ME_URI = "/api/v1/users/me"
        const val UNAVAILABLE_USER_ID = -1L
    }
}

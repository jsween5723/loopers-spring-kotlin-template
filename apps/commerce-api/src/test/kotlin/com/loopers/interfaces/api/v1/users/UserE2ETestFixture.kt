package com.loopers.interfaces.api.v1.users

import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.postForEntity

class UserE2ETestFixture(val testRestTemplate: TestRestTemplate) {

    fun 회원가입(request: UserRequest.Create = UserRequestGenerator.Create()): UserResponse.Create {
        val response = testRestTemplate.postForEntity<UserResponse.Create>(
            CREATE_URI,
            request,
        )
        return response.body!!
    }

    fun 사용자_지정(userId: Long) {
        testRestTemplate.restTemplate.interceptors.addFirst { request, body, execution ->
            request.headers.set("X-USER-ID", userId.toString())
            execution.execute(request, body)
        }
    }

    companion object {
        const val CREATE_URI = "/api/v1/users"
        const val GET_ME_URI = "/api/v1/users/me"
    }
}

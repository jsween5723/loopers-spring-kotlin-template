package com.loopers.interfaces.api.v1.users

import org.springframework.boot.test.web.client.TestRestTemplate

class UserE2ETestFixture(val testRestTemplate: TestRestTemplate) {

    companion object {
        const val CREATE_URI = "/api/v1/users"
    }
}

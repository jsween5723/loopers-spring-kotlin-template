package com.loopers.domain.user

import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class UserTest {

    @ParameterizedTest
    @ValueSource(
        strings = [
            "invalidusername",
            "invalid",
            "invalid_",
            "",
            "invalid!@#",
        ],
    )
    fun `ID 가 영문 및 숫자 10자 이내 형식에 맞지 않으면, User 객체 생성에 실패한다`(username: String) {
        // arrange
        val command = UserCommandGenerator.Create(username = username)
        // act
        // assert
        assertThrows<IllegalArgumentException> { command.toUser() }
    }
}

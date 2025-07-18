package com.loopers.domain.user

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@DisplayName("사용자 단위 테스트")
class UserTest {

    @ParameterizedTest
    @ValueSource(
        strings = [
            "invalidusername",
            "invalid111",
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

    @ParameterizedTest
    @ValueSource(
        strings = [
            "invalidemail",
            "invalid@",
            "",
            "invalid@example",
            "invalid@.com",
            "invalid@example.",
        ],
    )
    @DisplayName("이메일이 xx@yy.zz 형식에 맞지 않으면, User 객체 생성에 실패한다.")
    fun `이메일이 xx@yy_zz 형식에 맞지 않으면, User 객체 생성에 실패한다`(email: String) {
        // arrange
        val command = UserCommandGenerator.Create(email = email)
        // act
        // assert
        assertThrows<IllegalArgumentException> { command.toUser() }
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "2000-00-01",
            "2000-01-00",
            "2000-1-01",
            "2000-01-1",
            "20-01-01",
            "2000/01/01",
        ],
    )
    fun `생년월일이 yyyy-MM-dd 형식에 맞지 않으면, User 객체 생성에 실패한다`(birth: String) {
        // arrange
        val command = UserCommandGenerator.Create(birth = birth)
        // act
        // assert
        assertThrows<IllegalArgumentException> { command.toUser() }
    }
}

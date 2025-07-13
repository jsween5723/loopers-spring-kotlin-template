package com.loopers.domain.user

import com.loopers.application.user.UserService
import org.springframework.beans.factory.annotation.Autowired

class UserIntegrationTestFixture(@Autowired val userService: UserService) {

    fun 회원가입(command: UserCommand.Create = UserCommandGenerator.Create()): User = userService.create(command)

    companion object {
        const val NO_EXIST_USER_ID = -1L
    }
}

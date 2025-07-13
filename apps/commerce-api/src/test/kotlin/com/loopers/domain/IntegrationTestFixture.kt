package com.loopers.domain

import com.loopers.application.user.UserService
import com.loopers.domain.user.User
import com.loopers.domain.user.UserCommand
import com.loopers.domain.user.UserCommandGenerator
import org.springframework.beans.factory.annotation.Autowired

class IntegrationTestFixture(@Autowired val userService: UserService) {

    fun 회원가입(command: UserCommand.Create = UserCommandGenerator.Create()): User = userService.create(command)

    companion object {
        const val NO_EXIST_USER_ID = -1L
    }
}

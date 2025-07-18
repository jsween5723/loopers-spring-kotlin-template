package com.loopers.domain

import com.loopers.application.user.UserService
import com.loopers.domain.point.UserPoint
import com.loopers.domain.point.UserPointCommand
import com.loopers.domain.point.UserPointCommandGenerator
import com.loopers.domain.point.UserPointService
import com.loopers.domain.user.User
import com.loopers.domain.user.UserCommand
import com.loopers.domain.user.UserCommandGenerator

class IntegrationTestFixture(val userService: UserService, val userPointService: UserPointService) {
    private var userId: Long = NO_EXIST_USER_ID

    fun 회원가입(command: UserCommand.Create = UserCommandGenerator.Create()): User =
        userService.create(command)

    fun 기본_사용자_등록(): User = 회원가입().also {
        userId = it.id
    }

    fun 충전하기(
        command: UserPointCommand.Charge = UserPointCommandGenerator.Charge(
            userId = this.userId,
        ),
    ): UserPoint = userPointService.charge(command)

    companion object {
        const val NO_EXIST_USER_ID = -1L
    }
}

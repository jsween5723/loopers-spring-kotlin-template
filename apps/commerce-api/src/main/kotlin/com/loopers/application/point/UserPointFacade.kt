package com.loopers.application.point

import com.loopers.application.user.UserService
import com.loopers.domain.point.UserPoint
import com.loopers.domain.point.UserPointCommand
import org.springframework.stereotype.Component

@Component
class UserPointFacade(private val userPointService: UserPointService, private val userService: UserService) {
    fun charge(command: UserPointCommand.Charge): UserPoint {
        userService.read(command.userId) ?: throw NoSuchElementException("${command.userId}은 존재하지 않는 사용자입니다.")
        return userPointService.charge(command)
    }
}

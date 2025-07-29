package com.loopers.application.point

import com.loopers.application.user.UserService
import com.loopers.domain.point.UserPoint
import com.loopers.domain.point.UserPointCommand
import com.loopers.domain.point.UserPointService
import com.loopers.domain.user.UserId
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserPointFacade(private val userPointService: UserPointService, private val userService: UserService) {
    @Transactional
    fun charge(command: UserPointCommand.Charge): UserPoint {
        userService.read(command.userId) ?: throw NoSuchElementException("${command.userId}은 존재하지 않는 사용자입니다.")
        return userPointService.charge(command)
    }

    @Transactional(readOnly = true)
    fun getMe(userId: UserId): UserPoint? {
        userService.read(userId) ?: return null
        return userPointService.read(userId)
    }
}

package com.loopers.application.point

import com.loopers.domain.point.UserPoint
import com.loopers.domain.point.UserPointCommand
import com.loopers.domain.point.UserPointRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserPointService(private val repository: UserPointRepository) {
    @Transactional
    fun charge(command: UserPointCommand.Charge): UserPoint =
        with(command) {
            val point = repository.findByOrElsePersist(userId)
            point.charge(amount)
            repository.save(point)
        }
}

package com.loopers.domain.point

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserPointService(private val repository: UserPointRepository) {
    fun read(userId: Long): UserPoint = repository.findByOrElsePersist(userId)

    @Transactional
    fun charge(command: UserPointCommand.Charge): UserPoint =
        with(command) {
            val point = repository.findByOrElsePersist(userId)
            point.charge(amount)
            repository.save(point)
        }
}

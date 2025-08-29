package com.loopers.domain.point

import com.loopers.domain.user.UserId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.UUID

@Service
class UserPointService(private val repository: UserPointRepository) {
    fun read(userId: UserId): UserPoint = repository.findByOrElsePersist(userId)

    @Transactional
    fun charge(command: UserPointCommand.Charge): UserPoint =
        with(command) {
            val point = repository.findByOrElsePersist(userId)
            point.charge(amount)
            repository.save(point)
        }

    @Transactional
    fun reserve(eventId: UUID, userId: UserId, amount: BigDecimal): UserPointReserve {
        val point = repository.findByOrElsePersist(userId)
        val reservedItem = point.reserve(eventId, amount)
        return reservedItem
    }

    @Transactional
    fun cancelReserve(eventId: UUID, userId: UserId, amount: BigDecimal) {
        val point = repository.findByOrElsePersist(userId)
        point.cancelReserve(eventId, amount)
    }
}

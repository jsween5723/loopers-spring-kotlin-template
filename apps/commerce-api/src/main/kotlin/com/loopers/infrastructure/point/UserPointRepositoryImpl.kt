package com.loopers.infrastructure.point

import com.loopers.domain.point.UserPoint
import com.loopers.domain.point.UserPointRepository
import com.loopers.domain.user.UserId
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class UserPointRepositoryImpl(private val jpaRepository: UserPointJpaRepository) : UserPointRepository {
    override fun save(userPoint: UserPoint): UserPoint = jpaRepository.save(userPoint)

    @Transactional
    override fun findByOrElsePersist(userId: UserId): UserPoint {
        val userPoint = jpaRepository.findForUpdateByUserId(userId)
        if (userPoint != null) {
            return userPoint
        }
        jpaRepository.save(UserPoint(userId = userId))
        return jpaRepository.findForUpdateByUserId(userId)!!
    }
}

interface UserPointJpaRepository : JpaRepository<UserPoint, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findForUpdateByUserId(userId: UserId): UserPoint?
}

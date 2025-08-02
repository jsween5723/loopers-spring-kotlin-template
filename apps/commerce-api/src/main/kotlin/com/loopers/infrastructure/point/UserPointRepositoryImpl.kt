package com.loopers.infrastructure.point

import com.loopers.domain.point.UserPoint
import com.loopers.domain.point.UserPointRepository
import com.loopers.domain.user.UserId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
class UserPointRepositoryImpl(private val jpaRepository: UserPointJpaRepository) : UserPointRepository {
    override fun save(userPoint: UserPoint): UserPoint = jpaRepository.save(userPoint)

    override fun findByOrElsePersist(userId: UserId): UserPoint =
        jpaRepository.findByUserId(userId) ?: jpaRepository.save(UserPoint(userId = userId))
}

interface UserPointJpaRepository : JpaRepository<UserPoint, Long> {
    fun findByUserId(userId: UserId): UserPoint?
}

package com.loopers.infrastructure.point

import com.loopers.domain.point.UserPoint
import com.loopers.domain.point.UserPointRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class UserPointRepositoryImpl(private val jpaRepository: UserPointJpaRepository) : UserPointRepository {
    override fun save(userPoint: UserPoint): UserPoint = jpaRepository.save(userPoint)

    override fun findByOrElsePersist(userId: Long): UserPoint =
        jpaRepository.findByIdOrNull(userId) ?: jpaRepository.save(UserPoint(userId = userId))
}

interface UserPointJpaRepository : JpaRepository<UserPoint, Long>

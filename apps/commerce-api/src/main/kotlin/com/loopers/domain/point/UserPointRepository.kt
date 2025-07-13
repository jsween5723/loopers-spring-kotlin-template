package com.loopers.domain.point

interface UserPointRepository {
    fun save(userPoint: UserPoint): UserPoint
    fun findByOrElsePersist(userId: Long): UserPoint
}

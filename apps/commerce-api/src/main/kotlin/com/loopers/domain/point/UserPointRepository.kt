package com.loopers.domain.point

import com.loopers.domain.user.UserId

interface UserPointRepository {
    fun save(userPoint: UserPoint): UserPoint
    fun findByOrElsePersist(userId: UserId): UserPoint
}

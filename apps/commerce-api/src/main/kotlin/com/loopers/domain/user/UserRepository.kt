package com.loopers.domain.user

interface UserRepository {
    fun save(user: User): User
    fun findBy(userId: Long): User?
}

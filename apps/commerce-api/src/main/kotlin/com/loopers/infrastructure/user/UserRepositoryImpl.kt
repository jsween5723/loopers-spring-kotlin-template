package com.loopers.infrastructure.user

import com.loopers.domain.user.User
import com.loopers.domain.user.UserRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(private val jpaRepository: UserJpaRepository) : UserRepository {
    override fun save(user: User): User = jpaRepository.save(user)
    override fun findBy(userId: Long): User? = jpaRepository.findByIdOrNull(userId)
    override fun existsBy(username: String): Boolean = jpaRepository.existsByUsername(username)
}

interface UserJpaRepository : JpaRepository<User, Long> {
    fun existsByUsername(username: String): Boolean
}

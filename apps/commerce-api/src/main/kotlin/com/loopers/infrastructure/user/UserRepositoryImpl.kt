package com.loopers.infrastructure.user

import com.loopers.domain.user.User
import com.loopers.domain.user.UserRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(private val jpaRepository: UserJpaRepository) : UserRepository {
    override fun save(user: User): User = jpaRepository.save(user)
}

interface UserJpaRepository : JpaRepository<User, Long>

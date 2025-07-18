package com.loopers.application.user

import com.loopers.domain.user.User
import com.loopers.domain.user.UserCommand
import com.loopers.domain.user.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val repository: UserRepository) {
    fun read(userId: Long) = repository.findBy(userId)
    fun create(
        command: UserCommand.Create,
    ): User = with(command) {
        if (repository.existsBy(username)) {
            throw IllegalStateException("${username}은 이미 존재하는 로그인 ID입니다.")
        }
        repository.save(toUser())
    }
}

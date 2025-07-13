package com.loopers.application.user

import com.loopers.domain.user.User
import com.loopers.domain.user.UserCommand
import com.loopers.domain.user.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val repository: UserRepository) {
    fun create(command: UserCommand.Create): User = repository.save(command.toUser())
}

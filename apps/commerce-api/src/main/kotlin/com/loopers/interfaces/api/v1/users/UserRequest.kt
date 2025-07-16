package com.loopers.interfaces.api.v1.users

import com.loopers.domain.user.UserCommand
import jakarta.validation.constraints.NotBlank

class UserRequest {
    data class Create(
        @field:NotBlank
        val username: String,
        @field:NotBlank
        val name: String,
        @field:NotBlank
        val gender: String,
        @field:NotBlank
        val email: String,
        @field:NotBlank
        val birth: String,
    ) {
        fun toCommand() =
            UserCommand.Create(username = username, name = name, email = email, gender = gender, birth = birth)
    }
}

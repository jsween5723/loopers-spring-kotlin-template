package com.loopers.interfaces.api.v1.users

import com.loopers.domain.user.UserCommand
import jakarta.validation.constraints.NotBlank

class UserRequest {
    data class Create(
        @NotBlank
        val name: String,
        @NotBlank
        val gender: String,
        @NotBlank
        val email: String,
        @NotBlank
        val birth: String,
    ) {
        fun toCommand() =
            UserCommand.Create(name = name, email = email, gender = gender, birth = birth)
    }
}

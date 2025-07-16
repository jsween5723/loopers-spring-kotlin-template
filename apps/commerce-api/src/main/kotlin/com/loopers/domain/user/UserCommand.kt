package com.loopers.domain.user

object UserCommand {
    data class Create(val username: String, val name: String, val email: String, val gender: String, val birth: String) {
        fun toUser() =
            User(username = username, name = name, email = email, gender = gender, birth = birth)
    }
}

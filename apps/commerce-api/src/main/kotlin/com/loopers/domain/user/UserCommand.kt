package com.loopers.domain.user

class UserCommand private constructor() {
    data class Create(val name: String, val email: String, val gender: String, val birth: String) {
        fun toUser() =
            User(name = name, email = email, gender = gender, birth = birth)
    }
}

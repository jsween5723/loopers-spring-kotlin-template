package com.loopers.interfaces.api.v1.users

import com.loopers.domain.user.User

object UserResponse {
    data class Create(
        val id: Long,
        val name: String,
        val email: String,
        val birth: String,
        val gender: String,
        val username: String,
    ) {

        companion object {
            fun fromUser(user: User) = with(user) {
                Create(name = name, email = email, gender = gender, birth = birth, id = id, username = username)
            }
        }
    }

    data class GetMe(
        val id: Long,
        val name: String,
        val email: String,
        val birth: String,
        val gender: String,
        val username: String,
    ) {
        companion object {
            fun fromUser(user: User) = with(user) {
                GetMe(
                    id = id,
                    name = name,
                    email = email,
                    birth = birth,
                    gender = gender,
                    username = username,
                )
            }
        }
    }
}

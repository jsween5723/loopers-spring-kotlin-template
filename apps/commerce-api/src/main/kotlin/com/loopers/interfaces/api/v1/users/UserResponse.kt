package com.loopers.interfaces.api.v1.users

import com.loopers.domain.user.User

class UserResponse private constructor() {
    data class Create(val id: Long, val name: String, val email: String, val birth: String, val gender: String) {
        companion object {
            fun fromUser(user: User) = with(user) {
                Create(name = name, email = email, gender = gender, birth = birth, id = id)
            }
        }
    }
}

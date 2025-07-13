package com.loopers.domain.user

import com.loopers.domain.BaseEntity
import jakarta.persistence.Entity

@Entity(name = "users")
data class User(val name: String, val email: String, val gender: String, val birth: String) : BaseEntity()

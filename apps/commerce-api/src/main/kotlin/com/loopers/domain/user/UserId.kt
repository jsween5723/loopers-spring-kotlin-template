package com.loopers.domain.user

import jakarta.persistence.Column

@JvmInline
value class UserId(@Column(name = "user_id")val id: Long)

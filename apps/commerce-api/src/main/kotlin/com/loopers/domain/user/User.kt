package com.loopers.domain.user

import com.loopers.domain.BaseEntity
import jakarta.persistence.Entity
import org.hibernate.annotations.NaturalId

@Entity(name = "users")
data class User(@NaturalId val username: String, val name: String, val email: String, val gender: String, val birth: String) :
    BaseEntity()

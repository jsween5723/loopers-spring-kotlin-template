package com.loopers.domain.user

import com.loopers.domain.BaseEntity
import jakarta.persistence.Entity
import org.hibernate.annotations.NaturalId

@Entity(name = "users")
class User(@NaturalId val username: String, val name: String, val email: String, val gender: String, val birth: String) :
    BaseEntity() {
    init {
        require(USERNAME_REGEX.matches(username)) { "${username}이 영문 숫자 10자이내를 만족하지않습니다." }
        require(EMAIL_REGEX.matches(email)) { "${email}은 올바른 이메일 형식이 아닙니다." }
    }

    companion object {
        private val USERNAME_REGEX = "^[A-Za-z0-9]{1,9}\$".toRegex()
        private val EMAIL_REGEX = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,}\$".toRegex()
    }
}

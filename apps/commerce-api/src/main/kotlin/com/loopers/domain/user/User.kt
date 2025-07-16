package com.loopers.domain.user

import com.loopers.domain.BaseEntity
import jakarta.persistence.Entity
import org.hibernate.annotations.NaturalId
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Entity(name = "users")
class User(@NaturalId val username: String, val name: String, val email: String, val gender: String, val birth: String) :
    BaseEntity() {
    init {
        require(USERNAME_REGEX.matches(username)) { "${username}이 영문 숫자 10자이내를 만족하지않습니다." }
        require(EMAIL_REGEX.matches(email)) { "${email}은 올바른 이메일 형식이 아닙니다." }
        runCatching { LocalDate.parse(birth.trim(), DATE_FORMATTER) }
            .getOrElse { throw IllegalArgumentException("${birth}는 yyyy-MM-dd를 만족하지 않습니다.") }
    }

    companion object {
        private val USERNAME_REGEX = "^[A-Za-z0-9]{1,9}\$".toRegex()
        private val EMAIL_REGEX = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,}\$".toRegex()
        private val DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    }
}

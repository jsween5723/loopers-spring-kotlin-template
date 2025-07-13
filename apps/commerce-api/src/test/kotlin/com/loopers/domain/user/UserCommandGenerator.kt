package com.loopers.domain.user

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID
import kotlin.random.Random

object UserCommandGenerator {
    fun Create(
        username: String = generateUsername(),
        name: String = generateName(),
        gender: String = generateGender(),
        email: String = generateEmail(),
        birth: String = generateBirth(),
    ) = UserCommand.Create(username = username, name = name, gender = gender, email = email, birth = birth)

    private fun generateEmail(): String = "${UUID.randomUUID()}@example.com"
    private fun generateGender(): String = if (Random.nextBoolean()) "F" else "M"
    private fun generateName(): String = UUID.randomUUID()
        .toString()
        .slice(2..8)
    private fun generateUsername(): String = UUID.randomUUID().toString()
    private fun generateBirth(): String = LocalDate.ofEpochDay(
        Random.nextLong(
            LocalDate.of(1990, 1, 1)
                .toEpochDay(),
            LocalDate.now()
                .minusYears(14)
                .toEpochDay(),
        ),
    )
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
}

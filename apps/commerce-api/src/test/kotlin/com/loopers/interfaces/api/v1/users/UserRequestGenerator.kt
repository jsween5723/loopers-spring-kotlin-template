package com.loopers.interfaces.api.v1.users

import java.util.UUID
import kotlin.random.Random
import kotlin.random.nextInt

object UserRequestGenerator {
    fun Create(
        username: String = generateUsername(),
        name: String = generateName(),
        gender: String = generateGender(),
        email: String = generateEmail(),
        birth: String = generateBirth(),
    ) = UserRequest.Create(
        username = username,
        name = name,
        gender = gender,
        email = email,
        birth = birth,
    )

    private fun generateEmail(): String = "${UUID.randomUUID()}@example.com"
    private fun generateGender(): String = if (Random.nextBoolean()) "F" else "M"
    private fun generateName(): String = UUID.randomUUID()
        .toString()
        .slice(0..2)

    private fun generateUsername(): String = UUID.randomUUID()
        .toString()
        .slice(0..7)

    private fun generateBirth(): String = "${
        Random.nextInt(1990..2020)
    }-${"%02d".format(Random.nextInt(1..12))}-${"%02d".format(Random.nextInt(1..27))}"
}

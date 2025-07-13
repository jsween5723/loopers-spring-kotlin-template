package com.loopers.domain.point

import java.math.BigDecimal
import kotlin.random.Random

object UserPointCommandGenerator {
    fun Charge(userId: Long, amount: BigDecimal = generateAmount()) = UserPointCommand.Charge(amount = amount, userId = userId)
    private fun generateAmount() = Random.nextLong(1000, 10000).toBigDecimal()
}

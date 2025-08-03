package com.loopers.domain.point

import com.loopers.domain.user.UserId
import java.math.BigDecimal
import kotlin.random.Random

object UserPointCommandGenerator {
    fun Charge(
        userId: Long,
        amount: BigDecimal = generateAmount(),
    ) = UserPointCommand.Charge(amount = amount, userId = UserId(userId))
    private fun generateAmount() = Random.nextLong(1000, 10000).toBigDecimal()
}

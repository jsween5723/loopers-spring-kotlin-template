package com.loopers.interfaces.api.v1.points

import java.math.BigDecimal
import kotlin.random.Random

object PointRequestGenerator {
    fun Charge(amount: BigDecimal = generatePoint()) = UserPointRequest.Charge(amount = amount)
    private fun generatePoint() = Random.nextLong(1000, 100000).toBigDecimal()
}

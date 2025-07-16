package com.loopers.domain.point

import java.math.BigDecimal

object UserPointCommand {
    class Charge(val amount: BigDecimal, val userId: Long)
}

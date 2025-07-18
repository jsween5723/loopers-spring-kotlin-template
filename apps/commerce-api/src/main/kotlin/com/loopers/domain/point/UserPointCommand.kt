package com.loopers.domain.point

import java.math.BigDecimal

class UserPointCommand private constructor() {
    class Charge(val amount: BigDecimal, val userId: Long)
}

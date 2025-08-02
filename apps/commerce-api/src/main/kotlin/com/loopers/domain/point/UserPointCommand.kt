package com.loopers.domain.point

import com.loopers.domain.user.UserId
import java.math.BigDecimal

class UserPointCommand private constructor() {
    class Charge(val amount: BigDecimal, val userId: UserId)
}

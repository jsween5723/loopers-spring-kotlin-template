package com.loopers.interfaces.api.v1.points

import com.loopers.domain.auth.Authentication
import com.loopers.domain.point.UserPointCommand
import java.math.BigDecimal

class UserPointRequest private constructor() {
    data class Charge(val amount: BigDecimal) {
        fun toCommand(authentication: Authentication) = UserPointCommand.Charge(userId = authentication.id, amount = amount)
    }
}

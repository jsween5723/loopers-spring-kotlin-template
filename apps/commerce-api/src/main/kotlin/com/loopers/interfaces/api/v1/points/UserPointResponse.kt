package com.loopers.interfaces.api.v1.points

import com.loopers.domain.point.UserPoint
import java.math.BigDecimal

class UserPointResponse private constructor() {
    data class Charge(val point: BigDecimal) {
        companion object {
            fun fromUserPoint(entity: UserPoint): Charge = Charge(entity.point)
        }
    }
    data class GetMine(val point: BigDecimal) {
        companion object {
            fun fromUserPoint(entity: UserPoint): GetMine = GetMine(entity.point)
        }
    }
}

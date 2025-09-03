package com.loopers.domain.order

import java.math.BigDecimal

class Pricing(private val order: Order) {
    private var result: BigDecimal = order.totalPrice
    private var reservedPoint: ReservedPoint? = null
    fun applyPoint(reservedPoint: ReservedPoint): Pricing {
        check(this.reservedPoint == null) {"이미 포인트가 적용됐습니다."}
        this.reservedPoint = reservedPoint
        result -= reservedPoint.amount
        return this
    }

    fun applyCoupon(reservedIssuedCoupon: ReservedIssuedCoupon): Pricing {
        check(order.issuedCouponId == null) {"이미 쿠폰이 적용됐습니다."}
        order.applyIssuedCoupon(reservedIssuedCoupon)
        result = reservedIssuedCoupon.discountPolicy.discount(result)
        return this
    }
    fun complete(): BigDecimal {
        return this.result
    }
}

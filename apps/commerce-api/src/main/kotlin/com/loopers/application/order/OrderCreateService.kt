package com.loopers.application.order

import com.loopers.domain.coupon.IssuedCoupon
import com.loopers.domain.order.LineItem
import com.loopers.domain.order.Order

class OrderCreateService {
    fun create(lineItems: List<LineItem>, issuedCoupon: IssuedCoupon? = null): Order {
        val order = Order()
        order.changeTo(lineItems)
        if (issuedCoupon != null) {
            order.applyCoupon(issuedCoupon.id)
            issuedCoupon.use()
        }
        return order
    }
}

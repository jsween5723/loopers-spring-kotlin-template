package com.loopers.domain.order

import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertFailsWith

class PricingTest {
    @Test
    fun `주문에 포인트를 적용할 수 있다`() {
        //given
        val order = mockk<Order>()
        every { order.totalPrice }.returns(4000.toBigDecimal())
        val point = ReservedPoint(amount = 1000.toBigDecimal(), orderId = UUID.randomUUID())
        //when
        val actual = Pricing(order).applyPoint(point).complete()
        //then
        assertThat(actual).isEqualTo(3000.toBigDecimal())
    }

    @Test
    fun `주문에 쿠폰을 적용할 수 있다`() {
        //given
        val order = spyk<Order>()
        every { order.totalPrice }.returns(4000.toBigDecimal())
        val coupon = ReservedIssuedCoupon(orderId = UUID.randomUUID(), discountPolicy = DiscountPolicy.Fixed(2000.toBigDecimal()))
        //when
        val actual = Pricing(order).applyCoupon(coupon).complete()
        //then
        assertThat(actual).isEqualTo(2000.toBigDecimal())
    }

    @Test
    fun `주문에 포인트를 중복 적용하면 IllegalStateException이 발생한다`() {
        //given
        val order = mockk<Order>()
        every { order.totalPrice }.returns(4000.toBigDecimal())
        val point = ReservedPoint(amount = 1000.toBigDecimal(), orderId = UUID.randomUUID())
        //when
        //then
        assertFailsWith<IllegalStateException> {
            Pricing(order).applyPoint(point).applyPoint(point).complete()
        }
    }

    @Test
    fun `주문에 쿠폰을 중복적용하면 IllegalStateException이 발생한다`() {
        //given
        val order = spyk<Order>()
        every { order.totalPrice }.returns(4000.toBigDecimal())
        val coupon = ReservedIssuedCoupon(orderId = UUID.randomUUID(), discountPolicy = DiscountPolicy.Fixed(2000.toBigDecimal()))
        //when
        assertFailsWith<IllegalStateException> { Pricing(order).applyCoupon(coupon).applyCoupon(coupon).complete() }
        //then
    }
}

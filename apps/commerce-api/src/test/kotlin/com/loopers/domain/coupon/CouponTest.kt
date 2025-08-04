package com.loopers.domain.coupon

import com.loopers.domain.user.UserId
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class CouponTest {
    @Test
    fun `쿠폰은 재사용이 불가능합니다`() {
        // arrange
        val coupon = Coupon(name = "June McCoy", amount = 200.toBigDecimal(), type = Coupon.Type.FIXED, stock = 20)
        val issuedCoupon = coupon.issue(userId = UserId(1L))
        issuedCoupon.use()
        // act
        // assert
        assertThatThrownBy { issuedCoupon.use() }.isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun `쿠폰은 정액할인을 할 수 있습니다`() {
        // arrange
        val coupon = Coupon(name = "June McCoy", amount = 200.toBigDecimal(), type = Coupon.Type.FIXED, stock = 20)
        // act
        val actual = coupon.discount(400.toBigDecimal())
        // assert
        assertThat(actual).isEqualByComparingTo(200.toBigDecimal())
    }

    @Test
    fun `쿠폰은 정률할인을 할 수 있습니다`() {
        // arrange
        val coupon = Coupon(name = "June McCoy", amount = 10.toBigDecimal(), type = Coupon.Type.RATE, stock = 20)
        // act
        val actual = coupon.discount(400.toBigDecimal())
        // assert
        assertThat(actual).isEqualByComparingTo(360.toBigDecimal())
    }
}

package com.loopers.domain.order

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.assertFailsWith

class DiscountPolicyTest {
    @Test
    fun `정액 할인은 정액만큼 대상 금액을 차감한다`() {
        //given
        val policy = DiscountPolicy.Fixed(200.toBigDecimal())
        val amount = 4000.toBigDecimal()
        //when
        val actual = policy.discount(amount)
        //then
        assertThat(actual).isEqualTo(3800.toBigDecimal())
    }

    @Test
    fun `정액 할인은 원가를 초과하면 0미만으로 차감시키지 않는다`(){
        //given
        val policy = DiscountPolicy.Fixed(4200.toBigDecimal())
        val amount = 4000.toBigDecimal()
        //when
        val actual = policy.discount(amount)
        //then
        assertThat(actual).isEqualTo(0.toBigDecimal())
    }

    @Test
    fun `정률 할인은 비율을 적용해 할인한다`() {
        //given
        val policy = DiscountPolicy.Rate(50.toBigDecimal())
        val amount = 4000.toBigDecimal()
        //when
        val actual = policy.discount(amount)
        //then
        assertThat(actual).isEqualTo(2000.toBigDecimal())
    }

    @ParameterizedTest
    @ValueSource(ints = [-1, 101])
    fun `정률 할인이 0~100 범위를 초과하면 IllegalArgumentException을 발생시킨다`(rate: Int) {
        //given
        //when
        //then
        assertFailsWith<IllegalArgumentException> { DiscountPolicy.Rate(rate.toBigDecimal()) }
    }

    @Test
    fun `정액 할인이 0보다 작으면 IllegalArgumentException을 발생시킨다`() {
        //given
        val value = (-1).toBigDecimal()
        //when
        //then
        assertFailsWith<IllegalArgumentException> { DiscountPolicy.Fixed(value) }
    }

    @Test
    fun `할인 미적용시 값이 그대로 반환된다`(){
        //given
        val policy = DiscountPolicy.None()
        //when
        val actual = policy.discount(2000.toBigDecimal())
        //then
        assertThat(actual).isEqualTo(2000.toBigDecimal())
    }
}

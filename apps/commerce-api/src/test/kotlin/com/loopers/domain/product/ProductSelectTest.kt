package com.loopers.domain.product

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.instancio.Instancio
import org.instancio.Select.field
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime

class ProductSelectTest {

    @Test
    fun `상품은 선택할 때 선택 가능 상태가 아닐 경우 IllegalStateException을 던진다`() {
        // arrange
        val product = Instancio.of(Product::class.java)
            .set(field("state"), Product.State.UNAVAILABLE)
            .create()
        val quantity = 2L
        // act
        // assert
        assertThatThrownBy {
            product.select(quantity = quantity)
        }.isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun `상품을 선택할 때 선택 최대수량을 초과하는 경우 IllegalStateException을 던진다`() {
        // arrange
        val product = Instancio.of(Product::class.java)
            .set(field("maxQuantity"), 5L)
            .set(field("stock"), 10L)
            .set(field("state"), Product.State.AVAILABLE)
            .create()
        val quantity = 6L
        // act
        // assert
        assertThatThrownBy {
            product.select(quantity = quantity)
        }.isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun `상품을 선택할 때 재고 수량을 초과하는 경우 IllegalStateException을 던진다`() {
        // arrange
        val product = Instancio.of(Product::class.java)
            .set(field("maxQuantity"), 5L)
            .set(field("stock"), 3L)
            .set(field("state"), Product.State.AVAILABLE)
            .create()
        val quantity = 5L
        // act
        // assert
        assertThatThrownBy {
            product.select(quantity = quantity)
        }.isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun `상품은 선택 시 전시 일자 이전이면 IllegalStateException이 발생한다`() {
        // arrange
        val product = Instancio.of(Product::class.java)
            .set(field("maxQuantity"), 5L)
            .set(field("stock"), 3L)
            .set(
                field("displayedAt"),
                ZonedDateTime.now()
                    .plusDays(2),
            )
            .set(field("state"), Product.State.AVAILABLE)
            .create()
        val quantity = 3L
        // act
        // assert
        assertThatThrownBy {
            product.select(quantity = quantity)
        }.isInstanceOf(IllegalStateException::class.java)
    }

    fun createProduct(): Product = Instancio.of(Product::class.java)
            .set(field("maxQuantity"), 5L)
            .set(field("stock"), 3L)
            .set(
                field("displayedAt"),
                ZonedDateTime.now()
                    .minusDays(2),
            )
            .set(field("state"), Product.State.AVAILABLE)
            .create()
}

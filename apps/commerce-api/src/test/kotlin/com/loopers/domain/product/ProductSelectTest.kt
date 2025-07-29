package com.loopers.domain.product

import com.loopers.domain.brand.Brand
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.instancio.Instancio
import org.instancio.Select.field
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime

class ProductSelectTest {
    @Test
    fun `상품은 수량을 통해 선택하고 수량과 상품을 반환한다`() {
        // arrange
        val product = createProduct()
        val quantity = 2L
        // act
        val actual = product.select(quantity)
        // assert
        val expected = LineItem.from(product, 2L)
        assertThat(actual).isEqualTo(expected)
    }

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
    fun `상품을 선택할 때 브랜드가 입점 상태가 아닐 경우 IllegalStateException을 던진다`() {
        // arrange
        val brand = Instancio.of(Brand::class.java)
            .set(field("state"), Brand.State.CLOSED)
            .create()
        val product = Instancio.of(Product::class.java)
            .set(field("state"), Product.State.AVAILABLE)
            .set(field("brand"), brand)
            .create()
        // act
        // assert
        assertThatThrownBy {
            product.select(quantity = 2)
        }.isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun `상품을 선택할 때 선택 최대수량을 초과하는 경우 IllegalStateException을 던진다`() {
        // arrange
        val product = Instancio.of(Product::class.java)
            .set(field("maxQuantity"), 5L)
            .set(field("stock"), 10L)
            .set(field(Brand::class.java, "state"), Brand.State.OPENED)
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
            .set(field(Brand::class.java, "state"), Brand.State.OPENED)
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
            .set(field(Brand::class.java, "state"), Brand.State.OPENED)
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
            .set(field(Brand::class.java, "state"), Brand.State.OPENED)
            .set(
                field("displayedAt"),
                ZonedDateTime.now()
                    .minusDays(2),
            )
            .set(field("state"), Product.State.AVAILABLE)
            .create()
}

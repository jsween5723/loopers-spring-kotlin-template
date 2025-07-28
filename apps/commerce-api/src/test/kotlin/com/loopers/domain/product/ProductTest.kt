package com.loopers.domain.product

import com.loopers.domain.brand.Brand
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.instancio.Instancio
import org.instancio.Select.field
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime

class ProductTest {

    @Test
    fun `상품은 상품 식별자, 상품명, 가격, 옵션 목록으로 생성할 수 있다`() {
        // arrange
        // act
        // arrange
    }

    @Test
    fun `상품은 수량을 통해 출고하고 수량과 상품을 반환한다`() {
        // arrange
        val product = Instancio.of(Product::class.java)
            .create()
        val quantity = 2L
        // act
        val actual = product.sale(quantity)
        // assert
        val expected = LineItem.from(product, 2L)
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `상품은 출고할 때 출고 가능 상태가 아닐 경우 IllegalStateException을 던진다`() {
        // arrange
        val product = Instancio.of(Product::class.java)
            .set(field("state"), Product.State.UNAVAILABLE)
            .create()
        val quantity = 2L
        // act
        // assert
        assertThatThrownBy {
            product.sale(quantity = quantity)
        }.isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun `상품을 출고할 때 브랜드가 입점 상태가 아닐 경우 IllegalStateException을 던진다`() {
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
            product.sale(quantity = 2)
        }.isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun `상품을 출고할 때 출고 최대수량을 초과하는 경우 IllegalStateException을 던진다`() {
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
            product.sale(quantity = quantity)
        }.isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun `상품을 출고할 때 재고 수량을 초과하는 경우 IllegalStateException을 던진다`() {
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
            product.sale(quantity = quantity)
        }.isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun `상품은 출고 시 전시 일자 이전이면 IllegalStateException이 발생한다`() {
        // arrange
        val product = Instancio.of(Product::class.java)
            .set(field("maxQuantity"), 5L)
            .set(field("stock"), 3L)
            .set(field(Brand::class.java, "state"), Brand.State.OPENED)
            .set(field("displayedAt"), ZonedDateTime.now().plusDays(2))
            .set(field("state"), Product.State.AVAILABLE)
            .create()
        val quantity = 3L
        // act
        // assert
        assertThatThrownBy {
            product.sale(quantity = quantity)
        }.isInstanceOf(IllegalStateException::class.java)
    }
}

package com.loopers.domain.product

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.instancio.Instancio
import org.instancio.Select.field
import org.junit.jupiter.api.Test

class ProductTest {

    @Test
    fun `상품은 상품 식별자, 상품명, 가격, 등록일, 옵션 목록으로 생성할 수 있다`() {
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
        val actual = product.deduct(quantity)
        // assert
        val expected = LineItem(quantity = 2, product = product)
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
            product.deduct(quantity = quantity)
        }.isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun `상품을 출고할 때 브랜드가 입점 상태가 아닐 경우 IllegalStateException을 던진다`() {
        // arrange
        // act
        // assert
    }

    @Test
    fun `상품을 출고할 때 SKU의 출고 최대수량을 초과하는 경우 IllegalStateException을 던진다`() {
        // arrange
        // act
        // assert
    }

    @Test
    fun `상품을 출고할 때 SKU의 수량을 초과하는 경우 IllegalStateException을 던진다`() {
        // arrange
        // act
        // assert
    }
}

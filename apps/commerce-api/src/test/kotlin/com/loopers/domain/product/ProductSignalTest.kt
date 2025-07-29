package com.loopers.domain.product

import com.loopers.domain.brand.Brand
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime

class ProductSignalTest {
    lateinit var sut: ProductSignal

    @BeforeEach
    fun setUp() {
        sut = ProductSignal(
            product = Product(
                name = "Clement Olson",
                brand = Brand(name = "Dominique Whitley"),
                displayedAt = ZonedDateTime.now(),
                maxQuantity = 9015,
                price = 2000L.toBigDecimal(),
                stock = 7042,
            ),
        )
    }

    @Test
    fun `ProductSignal의 likeCount를 증가시킬 수 있다`() {
        // arrange
        // act
        sut.increaseLikeCount()
        // assert
        assertThat(sut.likeCount).isEqualTo(1L)
    }

    @Test
    fun `ProductSignal의 likeCount는 0보다 작아질 수 없다`() {
        // arrange
        // act
        // assert
        assertThatThrownBy { sut.decreaseLikeCount() }.isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun `ProductSignal의 likeCount를 감소시킬 수 있다`() {
        // arrange
        sut.increaseLikeCount()
        // act
        sut.decreaseLikeCount()
        // assert
        assertThat(sut.likeCount).isEqualTo(0L)
    }
}

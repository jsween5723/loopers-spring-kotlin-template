package com.loopers.domain.product

import com.loopers.domain.shared.ProductIdAndQuantity
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(private val repository: ProductRepository) {

    @Transactional
    fun selectProduct(item: ProductIdAndQuantity): LineItem {
        val product = repository.findByIdOrNull(item.productId)
            ?: throw EntityNotFoundException("${item.productId} 상품이 존재하지 않습니다.")
        return product.select(item.quantity)
    }

    @Transactional
    fun selectProducts(items: List<ProductIdAndQuantity>): List<LineItem> = items.map { selectProduct(it) }
}

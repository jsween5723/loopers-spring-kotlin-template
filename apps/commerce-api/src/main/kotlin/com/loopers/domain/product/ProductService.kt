package com.loopers.domain.product

import com.loopers.domain.shared.IdAndQuantity
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(private val productRepository: ProductRepository) {
    fun getByIds(ids: List<Long>): List<Product> = productRepository.getByIds(ids)

    @Transactional
    fun deduct(qty: IdAndQuantity) {
        val product = productRepository.findByIdForUpdate(qty.productId)
            ?: throw EntityNotFoundException("product를 찾을 수 없습니다.")
        product.deduct(qty.productId)
    }
}

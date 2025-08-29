package com.loopers.domain.product

import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class ProductService(private val productRepository: ProductRepository) {
    fun getByIds(ids: List<Long>): List<Product> = productRepository.getByIds(ids)

    @Transactional
    fun reserve(eventId: UUID, productId: Long, quantity: Long): ProductReserve {
        val product = productRepository.findByIdForUpdate(productId) ?: throw EntityNotFoundException()
        val reservedItem = product.reserve(eventId, quantity)
        productRepository.save(product)
        return reservedItem
    }

    @Transactional
    fun cancelReserve(eventId: UUID, productId: Long, quantity: Long) {
        val product = productRepository.findByIdForUpdate(productId) ?: throw EntityNotFoundException()
        product.cancelReserve(eventId, quantity)
    }

    @Transactional
    fun deduct(eventId: UUID, productId: Long, quantity: Long): Product {
        val product = productRepository.findByIdForUpdate(productId) ?: throw EntityNotFoundException()
        product.deduct(eventId, quantity)
        return product
    }
}

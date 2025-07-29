package com.loopers.domain.product

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductSignalService(private val repository: ProductSignalRepository) {
    fun findByProductOrElsePersist(product: Product): ProductSignal = repository.findByProduct(product)
        ?: repository.save(ProductSignal(product))

    @Transactional
    fun increaseLikeCount(product: Product) {
        val signal = findByProductOrElsePersist(product)
        signal.increaseLikeCount()
        repository.save(signal)
    }

    @Transactional
    fun decreaseLikeCount(product: Product) {
        val signal = findByProductOrElsePersist(product)
        signal.decreaseLikeCount()
        repository.save(signal)
    }
}

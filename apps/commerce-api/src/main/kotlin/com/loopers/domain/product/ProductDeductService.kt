package com.loopers.domain.product

import com.loopers.domain.shared.IdAndQuantity
import jakarta.persistence.EntityNotFoundException

class ProductDeductService {
    fun deduct(products: List<Product>, qtys: List<IdAndQuantity>): List<Product> {
        val productMap = products.associateBy { it.id }
        qtys.forEach {
            val product = productMap[it.productId] ?: throw EntityNotFoundException("${it.productId}는 존재하지 않는 상품입니다.")
            product.deduct(it.quantity)
        }
        return products
    }
}

package com.loopers.domain.product

import com.loopers.domain.BaseEntity
import jakarta.persistence.Entity

@Entity(name = "products")
class Product(val name: String, var stock: Long) : BaseEntity() {
    fun deduct(quantity: Long): LineItem {
        stock -= quantity
        return LineItem(product = this, quantity = quantity)
    }
}

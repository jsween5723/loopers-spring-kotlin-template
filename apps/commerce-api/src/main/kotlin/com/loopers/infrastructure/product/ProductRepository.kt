package com.loopers.infrastructure.product

import com.loopers.domain.product.Product
import com.loopers.domain.product.ProductRepository
import jakarta.persistence.EntityNotFoundException
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class ProductRepositoryImpl(private val jpaRepository: ProductJpaRepository) : ProductRepository {
    override fun getByIdsForUpdate(ids: List<Long>): List<Product> = ids.map {
        jpaRepository.findByIdForUpdate(it)
            ?: throw EntityNotFoundException("$it 는 없는 상품입니다.")
    }

    override fun getByIds(ids: List<Long>): List<Product> = ids.map {
        jpaRepository.findByIdOrNull(it)
        ?: throw EntityNotFoundException("$it 는 없는 상품입니다.")
    }

    override fun save(product: Product): Product = jpaRepository.save(product)
}

@Repository
interface ProductJpaRepository : JpaRepository<Product, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "SELECT p FROM products p WHERE p.id = :id")
    fun findByIdForUpdate(id: Long): Product?
}

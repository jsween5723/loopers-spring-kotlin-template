package com.loopers.infrastructure.product

import com.loopers.domain.product.ProductSignal
import com.loopers.domain.product.ProductSignalRepository
import jakarta.persistence.EntityNotFoundException
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.stereotype.Repository

@Repository
class ProductSignalRepositoryImpl(private val jpaRepository: ProductSignalJpaRepository) : ProductSignalRepository {
    override fun save(productSignal: ProductSignal): ProductSignal = jpaRepository.save(productSignal)

    override fun getByProductId(productId: Long): ProductSignal =
        jpaRepository.findByProductId(productId) ?: throw EntityNotFoundException("$productId 는 존재하지 않는 상품입니다.")

    override fun getForUpdateByProductId(productId: Long): ProductSignal =
        jpaRepository.findForUpdateByProductId(productId)
            ?: throw EntityNotFoundException("$productId 는 존재하지 않는 상품입니다.")
}

@Repository
interface ProductSignalJpaRepository : JpaRepository<ProductSignal, Long> {
    fun findByProductId(productId: Long): ProductSignal?

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @EntityGraph(attributePaths = ["product"])
    fun findForUpdateByProductId(productId: Long): ProductSignal?
}

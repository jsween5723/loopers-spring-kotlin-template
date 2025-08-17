package com.loopers.infrastructure.order

import com.loopers.domain.order.Order
import com.loopers.domain.order.OrderRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

@Component
class OrderRepositoryImpl(private val jpaRepository: OrderJpaRepository) : OrderRepository {
    override fun save(order: Order): Order = jpaRepository.save(order)

    override fun getById(id: Long) =
        jpaRepository.findByIdOrNull(id = id) ?: throw EntityNotFoundException("$id 는 존재하지 않는 주문 정보입니다.")
}

@Repository
interface OrderJpaRepository : JpaRepository<Order, Long> {
    @Query("select o from orders o join fetch o.orderLines where o.id = :id")
    fun findByIdOrNull(id: Long): Order?
}

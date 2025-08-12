package com.loopers.infrastructure.product

import com.linecorp.kotlinjdsl.dsl.jpql.Jpql
import com.linecorp.kotlinjdsl.dsl.jpql.sort.SortNullsStep
import com.linecorp.kotlinjdsl.querymodel.jpql.predicate.Predicatable
import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import com.loopers.domain.product.Product
import com.loopers.domain.product.ProductQuery
import com.loopers.domain.product.ProductSearchRepository
import com.loopers.domain.product.ProductSignal
import com.loopers.domain.product.SortFor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import javax.management.Query.eq

@Repository
class ProductSearchRepositoryImpl(private val jpaRepository: JpaProductRepository) : ProductSearchRepository {
    override fun search(query: ProductQuery): List<ProductSignal> =
        jpaRepository.findPage(pageable = query.pageable) {
            select(
                entity(ProductSignal::class),
            ).from(entity(ProductSignal::class), fetchJoin(path(ProductSignal::product)))
                .where(and(eqBrandId(query.brandId), path(Product::state).eq(Product.State.AVAILABLE)))
                .orderBy(
                    productSort(query.sort),
                )
        }
            .filterNotNull()

    private fun Jpql.eqBrandId(brandId: Long?): Predicatable? = brandId?.let { path(Product::brandId).eq(it) }

    private fun Jpql.productSort(sortBy: SortFor): SortNullsStep =
        when (sortBy) {
            SortFor.LATEST -> path(Product::displayedAt).desc()
            SortFor.PRICE_ASC -> path(Product::price).asc()
            SortFor.LIKES_ASC -> path(ProductSignal::likeCount).asc()
        }
}

interface JpaProductRepository :
    JpaRepository<ProductSignal, Long>,
    KotlinJdslJpqlExecutor

package com.loopers.interfaces.api.v1.products

import com.loopers.application.product.ProductFacade
import com.loopers.domain.auth.Authentication
import com.loopers.domain.product.ProductQuery
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/products")
class ProductController(private val productFacade: ProductFacade) {
    @GetMapping
    fun search(
        authentication: Authentication,
        query: ProductQuery,
    ): ProductFacade.Result = productFacade.search(authentication.userId, query)
}

package com.loopers.interfaces.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class HealthCheckController {
    @GetMapping
    fun healthCheck(): String = "Health Check"
}

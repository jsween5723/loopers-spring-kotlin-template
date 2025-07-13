package com.loopers.interfaces.api.v1.points

import com.loopers.application.point.UserPointFacade
import com.loopers.domain.auth.Authentication
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/points")
class UserPointController(private val userPointFacade: UserPointFacade) {
    @PatchMapping("charge")
    fun charge(authentication: Authentication, @RequestBody request: UserPointRequest.Charge): UserPointResponse.Charge =
        UserPointResponse.Charge.fromUserPoint(
            userPointFacade.charge(request.toCommand(authentication = authentication)),
        )
}

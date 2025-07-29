package com.loopers.interfaces.api.v1.points

import com.loopers.application.point.UserPointFacade
import com.loopers.domain.auth.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/points")
class UserPointController(private val userPointFacade: UserPointFacade) {
    @PostMapping("/charge")
    fun charge(authentication: Authentication, @RequestBody request: UserPointRequest.Charge): UserPointResponse.Charge =
        UserPointResponse.Charge.fromUserPoint(
            userPointFacade.charge(request.toCommand(authentication = authentication)),
        )

    @GetMapping
    fun getMine(authentication: Authentication): UserPointResponse.GetMine = UserPointResponse.GetMine.fromUserPoint(
        userPointFacade.getMe(authentication.userId) ?: throw NoSuchElementException("포인트 정보가 존재하지 않습니다."),
    )
}

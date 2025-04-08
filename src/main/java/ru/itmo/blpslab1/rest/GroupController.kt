package ru.itmo.blpslab1.rest

import arrow.core.Either
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.itmo.blpslab1.rest.dto.request.GroupRequest
import ru.itmo.blpslab1.service.GroupService
import ru.itmo.blpslab1.utils.core.toResponse
import java.util.UUID

@RestController
@RequestMapping("/api/groups")
class GroupController (
    private val groupService: GroupService
){

    @PostMapping("/")
    fun createGroup(
        @AuthenticationPrincipal userDetails: UserDetails,
        @Valid @RequestBody groupRequest: GroupRequest
    ) = groupService.createGroup(userDetails, groupRequest).toResponse()

    @GetMapping("/{id}")
    fun getGroup(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable(name = "id") id: UUID
    ) = groupService.getGroup(userDetails, id).toResponse()

    @PatchMapping("/{id}")
    fun editGroup(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable(name = "id") id: UUID,
        @Valid @RequestBody groupRequest: GroupRequest
    ) = groupRequest.copy(id = id)
        .run { groupService.editGroup(userDetails, this) }
        .toResponse()

    @DeleteMapping("/{id}")
    fun removeGroup(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable(name = "id") id: UUID
    ) = groupService.removeGroup(userDetails, id).toResponse()
}
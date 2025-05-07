package ru.itmo.blpslab1.rest

import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import ru.itmo.blpslab1.rest.dto.request.TariffRequest
import ru.itmo.blpslab1.service.TariffService
import java.util.UUID

@RestController
@RequestMapping("/api/tariff")
class TariffController(
    private val tariffService: TariffService
) {

    @PostMapping("/")
    fun createTariff(
        @AuthenticationPrincipal userDetails: UserDetails,
        @Valid @RequestBody tariffRequest: TariffRequest
    ) = tariffService.createTariff(userDetails, tariffRequest)

    @GetMapping("/{id}")
    fun getTariff(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable(name = "id") id: UUID
    ) = tariffService.getTariff(userDetails, id)

    @PatchMapping("/{id}")
    fun editTariff(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable(name = "id") id: UUID,
        @Valid @RequestBody tariffRequest: TariffRequest
    ) = tariffService.editTariff(userDetails, tariffRequest.copy(id = id))

    @DeleteMapping("/{id}")
    fun removeTariff(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable(name = "id") id: UUID
    ) = tariffService.removeTariff(userDetails, id)
}
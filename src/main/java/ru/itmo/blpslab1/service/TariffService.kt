package ru.itmo.blpslab1.service

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.userdetails.UserDetails
import ru.itmo.blpslab1.rest.dto.request.TariffRequest
import ru.itmo.blpslab1.rest.dto.response.TariffResponse
import ru.itmo.blpslab1.utils.service.Result
import java.util.UUID

interface TariffService {

    @PreAuthorize("hasAnyAuthority('TARIFF_CREATE', 'TARIFF_ADMIN')")
    fun createTariff(userDetails: UserDetails, tariffRequest: TariffRequest): Result<TariffResponse>

    @PreAuthorize("hasAnyAuthority('TARIFF_VIEW', 'TARIFF_ADMIN')")
    fun getTariff(userDetails: UserDetails, id: UUID): Result<TariffResponse>

    @PreAuthorize("hasAnyAuthority('TARIFF_EDIT', 'TARIFF_ADMIN')")
    fun editTariff(userDetails: UserDetails, tariffRequest: TariffRequest): Result<TariffResponse>

    @PreAuthorize("hasAnyAuthority('TARIFF_DELETE', 'TARIFF_ADMIN')")
    fun removeTariff(userDetails: UserDetails, id: UUID): Result<Unit>
}
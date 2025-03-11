package ru.itmo.blpslab1.service

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.userdetails.UserDetails
import ru.itmo.blpslab1.rest.dto.request.GroupRequest
import ru.itmo.blpslab1.rest.dto.response.GroupResponse
import ru.itmo.blpslab1.utils.service.Result
import java.util.UUID

interface GroupService {
    @PreAuthorize("hasAnyAuthority('GROUP_CREATE', 'GROUP_ADMIN')")
    fun createGroup(userDetails: UserDetails, groupRequest: GroupRequest): Result<GroupResponse>

    @PreAuthorize("hasAnyAuthority('GROUP_VIEW', 'GROUP_ADMIN')")
    fun getGroup(userDetails: UserDetails, id: UUID): Result<GroupResponse>

    @PreAuthorize("hasAnyAuthority('GROUP_EDIT', 'GROUP_ADMIN')")
    fun editGroup(userDetails: UserDetails, groupRequest: GroupRequest): Result<GroupResponse>

    @PreAuthorize("hasAnyAuthority('GROUP_REMOVE', 'GROUP_ADMIN')")
    fun removeGroup(userDetails: UserDetails, id: UUID): Result<Unit>
}
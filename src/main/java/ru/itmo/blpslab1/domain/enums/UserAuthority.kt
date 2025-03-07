package ru.itmo.blpslab1.domain.enums;

import org.springframework.security.core.GrantedAuthority


enum class UserAuthority(
    private val authority: String
): GrantedAuthority {
    GROUP_VIEW("GROUP_VIEW"),
    GROUP_EDIT("GROUP_EDIT"),
    GROUP_CREATE("GROUP_CREATE");

    override fun getAuthority(): String = authority
}

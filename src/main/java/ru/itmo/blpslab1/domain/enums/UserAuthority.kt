package ru.itmo.blpslab1.domain.enums;

import org.springframework.security.core.GrantedAuthority


enum class UserAuthority(
    private val authority: String
): GrantedAuthority {
    /*
    Groups
     */
    GROUP_VIEW("GROUP_VIEW"),
    GROUP_EDIT("GROUP_EDIT"),
    GROUP_CREATE("GROUP_CREATE"),

    /*
    Goals
     */
    GOAL_ADMIN("GOAL_ADMIN"),
    GOAL_CREATE("GOAL_CREATE"),
    GOAL_VIEW("GOAL_VIEW"),
    GOAL_EDIT("GOAL_EDIT"),
    GOAL_DELETE("GOAL_DELETE"),
    ;

    override fun getAuthority(): String = authority
}

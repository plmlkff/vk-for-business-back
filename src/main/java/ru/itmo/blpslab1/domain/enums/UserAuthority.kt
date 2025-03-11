package ru.itmo.blpslab1.domain.enums;

import org.springframework.security.core.GrantedAuthority


enum class UserAuthority: GrantedAuthority {
    /*
    Groups
     */
    GROUP_ADMIN,
    GROUP_CREATE,
    GROUP_VIEW,
    GROUP_EDIT,
    GROUP_DELETE,

    /*
    Goals
     */
    GOAL_ADMIN,
    GOAL_CREATE,
    GOAL_VIEW,
    GOAL_EDIT,
    GOAL_DELETE,

    /*
    Card credential
     */
    CARD_CREDENTIAL_ADMIN,
    CARD_CREDENTIAL_CREATE,
    CARD_CREDENTIAL_VIEW,
    CARD_CREDENTIAL_EDIT,
    CARD_CREDENTIAL_DELETE,
    ;

    override fun getAuthority(): String = name
}

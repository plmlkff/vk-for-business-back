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


    /*
    Promotion task
     */
    PROMOTION_TASK_ADMIN,
    PROMOTION_TASK_CREATE,
    PROMOTION_TASK_VIEW,
    PROMOTION_TASK_EDIT,
    PROMOTION_TASK_DELETE,


    /*
    Subscription
     */
    SUBSCRIPTION_ADMIN,
    SUBSCRIPTION_CREATE,
    SUBSCRIPTION_VIEW,
    SUBSCRIPTION_EDIT,
    SUBSCRIPTION_DELETE,


    /*
    Tariff
     */
    TARIFF_ADMIN,
    TARIFF_CREATE,
    TARIFF_VIEW,
    TARIFF_EDIT,
    TARIFF_DELETE,


    /*
    Transaction
     */
    TRANSACTION_ADMIN,
    TRANSACTION_CREATE,

    /*
    User
     */
    USER_ADMIN,
    USER_CREATE,
    USER_VIEW,
    USER_EDIT,
    USER_DELETE,

    /*
    UserRole
     */
    USER_ROLE_ADMIN,
    USER_ROLE_CREATE,
    USER_ROLE_VIEW,
    USER_ROLE_EDIT,
    USER_ROLE_DELETE,
    ;

    override fun getAuthority(): String = name
}

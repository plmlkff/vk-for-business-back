package ru.itmo.blpslab1.domain.repository.specification

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import ru.itmo.blpslab1.domain.entity.User
import ru.itmo.blpslab1.domain.entity.UserRole
import ru.itmo.blpslab1.rest.dto.request.query.UserQuery
import ru.itmo.blpslab1.utils.core.isNotEmpty
import java.util.UUID

class UserSpecification(
    private val userQuery: UserQuery
): Specification<User> {

    override fun toPredicate(
        root: Root<User>,
        query: CriteriaQuery<*>?,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {
        var resPredicate = criteriaBuilder.and()
        if (isNotEmpty(userQuery.ids)){
            val idsPredicate = root.get<UUID>(User.Fields.ID).`in`(userQuery.ids)
            resPredicate = criteriaBuilder.and(resPredicate, idsPredicate)
        }
        if (isNotEmpty(userQuery.roles)){
            val rolesPredicate = root.join<User, UserRole>(User.Fields.ROLES).get<String>(UserRole.Fields.NAME).`in`(userQuery.roles!!.map { it.name })
            resPredicate = criteriaBuilder.and(resPredicate, rolesPredicate)
        }
        return resPredicate
    }
}
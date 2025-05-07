package ru.itmo.blpslab1.domain.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.itmo.blpslab1.domain.entity.UserRole
import java.util.UUID

@Repository
interface UserRoleRepository: JpaRepository<UserRole, UUID>{

    @Query("""
        SELECT ur FROM UserRole ur
        WHERE ur.name = :name
    """)
    fun findUserRoleByName(@Param("name") name: String): UserRole?
}
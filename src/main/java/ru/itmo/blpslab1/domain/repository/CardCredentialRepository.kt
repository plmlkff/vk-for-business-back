package ru.itmo.blpslab1.domain.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.itmo.blpslab1.domain.entity.CardCredential
import java.util.UUID

@Repository
interface CardCredentialRepository: JpaRepository<CardCredential, UUID> {

    @Query("""
        SELECT cc
        FROM CardCredential cc
        WHERE cc.owner.login = :username
    """)
    fun findAllByOwner(@Param("username") username: String): List<CardCredential>
}
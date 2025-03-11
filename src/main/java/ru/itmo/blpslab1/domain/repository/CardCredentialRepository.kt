package ru.itmo.blpslab1.domain.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.itmo.blpslab1.domain.entity.CardCredential
import java.util.UUID

@Repository
interface CardCredentialRepository: JpaRepository<CardCredential, UUID>
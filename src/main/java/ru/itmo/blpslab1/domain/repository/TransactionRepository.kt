package ru.itmo.blpslab1.domain.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.itmo.blpslab1.domain.entity.Transaction
import java.util.UUID

interface TransactionRepository:JpaRepository<Transaction, UUID>
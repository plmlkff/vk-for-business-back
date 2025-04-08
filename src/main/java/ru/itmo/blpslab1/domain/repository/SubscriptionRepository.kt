package ru.itmo.blpslab1.domain.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.itmo.blpslab1.domain.entity.Subscription
import java.util.UUID

@Repository
interface SubscriptionRepository: JpaRepository<Subscription, UUID> {

    @Query(
        """
            SELECT s
            FROM Subscription s
            WHERE s.owner.login = :username
        """
    )
    fun findAllByOwner(@Param("username") username: String): List<Subscription>
}
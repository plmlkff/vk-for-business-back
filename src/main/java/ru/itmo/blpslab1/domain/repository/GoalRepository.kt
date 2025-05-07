package ru.itmo.blpslab1.domain.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.itmo.blpslab1.domain.entity.Goal
import java.util.UUID

@Repository
interface GoalRepository: JpaRepository<Goal, UUID> {

    @Query(
        """
            SELECT g
            FROM Goal g
            WHERE g.group.id = :groupId
        """
    )
    fun findAllByGroupId(
        @Param("groupId") groupId: UUID
    ): List<Goal>
}
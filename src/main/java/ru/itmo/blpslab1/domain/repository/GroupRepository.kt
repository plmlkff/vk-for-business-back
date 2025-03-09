package ru.itmo.blpslab1.domain.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.itmo.blpslab1.domain.entity.Group
import java.util.*

@Repository
interface GroupRepository : JpaRepository<Group, UUID> {

    @Query(
        """
            SELECT g FROM Group g
            WHERE g.owner.id = :id
        """
    )
    fun findAllByOwnerId(@Param("id") id: UUID): List<Group>
}
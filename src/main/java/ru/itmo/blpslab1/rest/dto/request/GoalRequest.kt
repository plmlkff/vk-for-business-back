package ru.itmo.blpslab1.rest.dto.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size
import ru.itmo.blpslab1.domain.entity.Goal
import java.util.UUID

data class GoalRequest(
    val id: UUID?,
    @field:Size(min = 1)
    val name: String,
    @field:Min(0)
    val targetSum: Double,
    @field:Min(0)
    val currentSum: Double
)

fun GoalRequest.toDomain() = Goal().let {
    it.id = this.id
    it.name = this.name
    it.targetSum = this.targetSum
    it.currentSum = this.currentSum
    it
}

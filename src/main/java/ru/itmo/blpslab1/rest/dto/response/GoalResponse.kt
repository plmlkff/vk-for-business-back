package ru.itmo.blpslab1.rest.dto.response

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size
import ru.itmo.blpslab1.domain.entity.Goal
import java.util.*

data class GoalResponse(
    val id: UUID,
    @Size(min = 1)
    val name: String,
    @Min(0)
    val targetSum: Double,
    @Min(0)
    val currentSum: Double
){
    companion object
}

fun Goal.toResponse() = GoalResponse(
    id = this.id,
    name = this.name,
    targetSum = this.targetSum,
    currentSum = this.currentSum
)

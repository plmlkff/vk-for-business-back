import ru.itmo.blpslab1.rest.dto.response.toResponse

import org.springframework.http.HttpStatus.*
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import ru.itmo.blpslab1.domain.repository.GroupRepository
import ru.itmo.blpslab1.domain.repository.PromotionTaskRepository
import ru.itmo.blpslab1.domain.repository.UserRepository
import ru.itmo.blpslab1.rest.dto.request.PromotionTaskRequest
import ru.itmo.blpslab1.rest.dto.request.toDomain
import ru.itmo.blpslab1.rest.dto.response.PromotionTaskResponse
import ru.itmo.blpslab1.service.PromotionTaskService
import ru.itmo.blpslab1.utils.core.hasNoAccessTo
import ru.itmo.blpslab1.utils.service.Result
import java.util.UUID
import ru.itmo.blpslab1.utils.service.*
import kotlin.jvm.optionals.getOrNull

@Service
class PromotionTaskServiceImpl(
    private val promotionTaskRepository: PromotionTaskRepository,
    userRepository: UserRepository,
    private val groupRepository: GroupRepository,
    repository: GroupRepository,
    taskRepository: PromotionTaskRepository
): PromotionTaskService {

    override fun createPromotionTask(
        userDetails: UserDetails,
        promotionTaskRequest: PromotionTaskRequest
    ): Result<PromotionTaskResponse> {
        if (promotionTaskRequest.id != null) return error()

        val dbGroup = groupRepository.findById(promotionTaskRequest.groupId).getOrNull() ?: return error(NOT_FOUND)

        if (userDetails hasNoAccessTo dbGroup) return error(METHOD_NOT_ALLOWED)

        val promotionTask = promotionTaskRequest.toDomain()
        promotionTask.apply {
            group = dbGroup
        }

        return ok(promotionTaskRepository.save(promotionTask).toResponse())
    }

    override fun getPromotionTask(userDetails: UserDetails, id: UUID): Result<PromotionTaskResponse> {
        TODO("Not yet implemented")
    }

    override fun editPromotionTask(
        userDetails: UserDetails,
        promotionTaskRequest: PromotionTaskRequest
    ): Result<PromotionTaskResponse> {
        TODO("Not yet implemented")
    }

    override fun removePromotionTask(userDetails: UserDetails, id: UUID): Result<Unit> {
        TODO("Not yet implemented")
    }
}
import ru.itmo.blpslab1.rest.dto.response.toResponse

import org.springframework.http.HttpStatus.*
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.itmo.blpslab1.domain.repository.GroupRepository
import ru.itmo.blpslab1.domain.repository.PromotionTaskRepository
import ru.itmo.blpslab1.domain.repository.UserRepository
import ru.itmo.blpslab1.rest.dto.request.PromotionTaskRequest
import ru.itmo.blpslab1.rest.dto.request.toDomain
import ru.itmo.blpslab1.rest.dto.response.PromotionTaskResponse
import ru.itmo.blpslab1.service.PromotionTaskService
import ru.itmo.blpslab1.service.minio.UserImageService
import ru.itmo.blpslab1.utils.core.hasNoAccessTo
import ru.itmo.blpslab1.utils.service.Result
import java.util.UUID
import ru.itmo.blpslab1.utils.service.*
import kotlin.jvm.optionals.getOrNull

@Service
class PromotionTaskServiceImpl(
    private val promotionTaskRepository: PromotionTaskRepository,
    private val groupRepository: GroupRepository,
    private val userImageService: UserImageService
): PromotionTaskService {

    @Transactional
    override fun createPromotionTask(
        userDetails: UserDetails,
        promotionTaskRequest: PromotionTaskRequest
    ): Result<PromotionTaskResponse> {
        if (promotionTaskRequest.id != null) return error()

        val dbGroup = groupRepository.findById(promotionTaskRequest.groupId).getOrNull() ?: return error(NOT_FOUND)

        if (userDetails hasNoAccessTo dbGroup) return error(METHOD_NOT_ALLOWED)

        val imageRequest = promotionTaskRequest.image
        val dbImageName = userImageService.saveImage(imageRequest.name, imageRequest.bytes).getOrNull() ?: return error()

        val promotionTask = promotionTaskRequest.toDomain()
        promotionTask.apply {
            group = dbGroup
            imageName = dbImageName
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
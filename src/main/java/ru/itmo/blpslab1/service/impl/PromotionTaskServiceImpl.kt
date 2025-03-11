import ru.itmo.blpslab1.rest.dto.response.toResponse

import org.springframework.http.HttpStatus.*
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.itmo.blpslab1.domain.repository.GroupRepository
import ru.itmo.blpslab1.domain.repository.PromotionTaskRepository
import ru.itmo.blpslab1.rest.dto.request.PromotionTaskRequest
import ru.itmo.blpslab1.rest.dto.request.toDomain
import ru.itmo.blpslab1.rest.dto.response.PromotionTaskResponse
import ru.itmo.blpslab1.service.PromotionTaskService
import ru.itmo.blpslab1.service.exceptions.RollbackTransactionException
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
        request: PromotionTaskRequest
    ): Result<PromotionTaskResponse> {
        if (request.id != null) return error()

        val dbGroup = groupRepository.findById(request.groupId).getOrNull() ?: return error(NOT_FOUND)

        if (userDetails hasNoAccessTo dbGroup) return error(METHOD_NOT_ALLOWED)

        if(request.image == null) {
            val promotionTask = promotionTaskRepository.save(request.toDomain())
            return ok(promotionTask.apply { group = dbGroup }.toResponse())
        }

        val uploadImageContinuable = userImageService.saveImage(request.image)

        val promotionTask = request.toDomain().apply {
            group = dbGroup
            imageName = uploadImageContinuable.uniqueFileName
        }

        promotionTaskRepository.save(promotionTask)

        uploadImageContinuable.`continue`().getOrNull() ?: throw RollbackTransactionException(SERVICE_UNAVAILABLE)

        return ok(promotionTask.toResponse())
    }

    override fun getPromotionTask(userDetails: UserDetails, id: UUID): Result<PromotionTaskResponse> {
        val promotionTask = promotionTaskRepository.findById(id).getOrNull() ?: return error(NOT_FOUND)

        promotionTask.imageName ?: return ok(promotionTask.toResponse())

        val dbImage = userImageService.getImage(promotionTask.imageName) ?: return error(SERVICE_UNAVAILABLE)

        return ok(promotionTask.toResponse().copy(image = dbImage))
    }

    @Transactional
    override fun editPromotionTask(
        userDetails: UserDetails,
        request: PromotionTaskRequest
    ): Result<PromotionTaskResponse> {
        request.id ?: return error()

        val dbPromotionTask = promotionTaskRepository.findById(request.id).getOrNull() ?: return error(NOT_FOUND)

        if (userDetails hasNoAccessTo dbPromotionTask.group) return error(METHOD_NOT_ALLOWED)

        val res = promotionTaskRepository.save(request.toDomain().apply { group = dbPromotionTask.group })

        if (request.image == null) return ok(res.toResponse())

        val newImageName = userImageService.updateImage(request.image, dbPromotionTask.imageName)

        return ok(promotionTaskRepository.save(res.apply { imageName = newImageName }).toResponse())
    }

    @Transactional
    override fun removePromotionTask(userDetails: UserDetails, id: UUID): Result<Unit> {
        val promotionTask = promotionTaskRepository.findById(id).getOrNull() ?: return error(NOT_FOUND)

        promotionTaskRepository.delete(promotionTask)

        userImageService.removeImage(promotionTask.imageName)

        return ok()
    }
}
package ru.itmo.blpslab1.service.impl

import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus.*
import ru.itmo.blpslab1.utils.service.*
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import ru.itmo.blpslab1.domain.repository.CardCredentialRepository
import ru.itmo.blpslab1.domain.repository.GroupRepository
import ru.itmo.blpslab1.domain.repository.TariffRepository
import ru.itmo.blpslab1.rest.dto.request.TariffRequest
import ru.itmo.blpslab1.rest.dto.request.toDomain
import ru.itmo.blpslab1.rest.dto.response.TariffResponse
import ru.itmo.blpslab1.rest.dto.response.toResponse
import ru.itmo.blpslab1.service.TariffService
import ru.itmo.blpslab1.service.minio.UserImageService
import ru.itmo.blpslab1.utils.core.hasNoAccessTo
import ru.itmo.blpslab1.utils.core.test
import ru.itmo.blpslab1.utils.service.Result
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@Service
class TariffServiceImpl(
    private val tariffRepository: TariffRepository,
    private val userImageService: UserImageService,
    private val groupRepository: GroupRepository,
    private val cardCredentialRepository: CardCredentialRepository
): TariffService {
    @Transactional
    override fun createTariff(userDetails: UserDetails, tariffRequest: TariffRequest): Result<TariffResponse> {
        if (tariffRequest.id != null) return error()

        val dbGroup = groupRepository.findById(tariffRequest.groupId).getOrNull() ?: return error(NOT_FOUND)
        val cardCredential = cardCredentialRepository.findById(tariffRequest.recipientCardId).getOrNull() ?: return error(NOT_FOUND)
        if (userDetails hasNoAccessTo dbGroup) return error(METHOD_NOT_ALLOWED)

        var tariff = tariffRepository.save(tariffRequest.toDomain().apply {
            group = dbGroup
            recipientCard = cardCredential
        })

        if (tariffRequest.previewImage == null) return ok(tariff.toResponse())

        val uploadImageContinuable = userImageService.saveImage(tariffRequest.previewImage)
        tariff = tariffRepository.save(tariff.apply { previewImageName = uploadImageContinuable.uniqueFileName })
        uploadImageContinuable.`continue`()

        return ok(tariff.toResponse())
    }

    override fun getTariff(userDetails: UserDetails, id: UUID): Result<TariffResponse> {
        val tariff = tariffRepository.findById(id).getOrNull() ?: return error(NOT_FOUND)

        if (userDetails hasNoAccessTo tariff.group) return error(METHOD_NOT_ALLOWED)

        tariff.previewImageName ?: return ok(tariff.toResponse())

        val dbImage = userImageService.getImage(tariff.previewImageName) ?: return error(SERVICE_UNAVAILABLE)

        return ok(tariff.toResponse().apply { previewImage = dbImage })
    }

    @Transactional
    override fun editTariff(userDetails: UserDetails, tariffRequest: TariffRequest): Result<TariffResponse> {
        if (tariffRequest.id == null) return error()

        var dbTariff = tariffRepository.findById(tariffRequest.id).getOrNull() ?: return error(NOT_FOUND)

        if (userDetails hasNoAccessTo dbTariff.group) return error(METHOD_NOT_ALLOWED)

        dbTariff = tariffRepository.save(tariffRequest.toDomain().apply { group = dbTariff.group })

        if (tariffRequest.previewImage == null) return ok(dbTariff.toResponse())

        val newImageName = userImageService.updateImage(tariffRequest.previewImage, dbTariff.previewImageName)

        return ok(tariffRepository.save(dbTariff.apply { previewImageName = newImageName }).toResponse())
    }

    @Transactional
    override fun removeTariff(
        userDetails: UserDetails,
        id: UUID
    ) = tariffRepository.findById(id).getOrNull().test(
        condition = {it != null},
        onTrue = {
            if (userDetails hasNoAccessTo it!!.group) return@test error(NOT_FOUND)

            tariffRepository.delete(it)
            ok(userImageService.removeImage(it.previewImageName))
        },
        onFalse = { error(NOT_FOUND) }
    )
}
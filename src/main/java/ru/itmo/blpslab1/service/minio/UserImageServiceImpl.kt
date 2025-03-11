package ru.itmo.blpslab1.service.minio

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import ru.itmo.blpslab1.config.MinioConfig
import ru.itmo.blpslab1.core.minio.MinioFilesManager
import ru.itmo.blpslab1.rest.dto.request.ImageRequest
import ru.itmo.blpslab1.service.exceptions.RollbackTransactionException

@Service
class UserImageServiceImpl(
    val minioFilesManager: MinioFilesManager, val minioConfig: MinioConfig
) : UserImageService {
    override fun saveImage(
        imageRequest: ImageRequest
    ) = try {
        minioFilesManager.upload(imageRequest.name, imageRequest.bytes, minioConfig.bucket)
    } catch (e: Throwable) {
        throw RollbackTransactionException(HttpStatus.SERVICE_UNAVAILABLE)
    }

    override fun getImage(
        name: String
    ) = try {
        minioFilesManager.getByName(name, minioConfig.bucket)
    } catch (e: Throwable) {
        throw RollbackTransactionException(HttpStatus.SERVICE_UNAVAILABLE)
    }

    override fun removeImage(
        name: String
    ) = try {
        minioFilesManager.remove(name, minioConfig.bucket)
    } catch (e: Throwable) {
        throw RollbackTransactionException(HttpStatus.SERVICE_UNAVAILABLE)
    }

    override fun updateImage(
        imageRequest: ImageRequest, oldImageUniqueName: String?
    ) = try {
        if (oldImageUniqueName != null) minioFilesManager.remove(oldImageUniqueName, minioConfig.bucket)

        val continuable = minioFilesManager.upload(imageRequest.name, imageRequest.bytes, minioConfig.bucket)

        continuable.`continue`()

        continuable.uniqueFileName
    } catch (e: Throwable) {
        throw RollbackTransactionException(HttpStatus.SERVICE_UNAVAILABLE)
    }
}
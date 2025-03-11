package ru.itmo.blpslab1.service.minio

import arrow.core.left
import arrow.core.right
import org.springframework.stereotype.Service
import ru.itmo.blpslab1.config.MinioConfig
import ru.itmo.blpslab1.core.minio.MinioFilesManager

@Service
class UserImageServiceImpl(
    val minioFilesManager: MinioFilesManager,
    val minioConfig: MinioConfig
) : UserImageService {
    override fun saveImage(
        name: String,
        bytes: ByteArray
    ) = try {
        minioFilesManager.upload(name, bytes, minioConfig.bucket).right()
    } catch (e: Throwable) {
        e.left()
    }

    override fun getImage(
        name: String
    ) = try {
        minioFilesManager.getByName(name, minioConfig.bucket).right()
    } catch (e: Throwable) {
        e.left()
    }

}
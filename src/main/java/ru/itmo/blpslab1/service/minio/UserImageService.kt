package ru.itmo.blpslab1.service.minio

import ru.itmo.blpslab1.core.minio.MinioFilesManager
import ru.itmo.blpslab1.rest.dto.request.ImageRequest

interface UserImageService {
    fun saveImage(imageRequest: ImageRequest): MinioFilesManager.ContinuableFileUploadTask

    fun getImage(name: String): ByteArray?

    fun removeImage(name: String)

    fun updateImage(newImage: ImageRequest, oldImageUniqueName: String?): String
}
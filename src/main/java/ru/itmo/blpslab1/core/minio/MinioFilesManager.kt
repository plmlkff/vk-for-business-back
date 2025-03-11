package ru.itmo.blpslab1.core.minio

import io.minio.GetObjectArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import org.springframework.stereotype.Component
import java.io.ByteArrayInputStream
import java.util.UUID

@Component
class MinioFilesManager(
    val minioClient: MinioClient
) {
    private val UUID_LENGTH = 36

    fun getByName(fileName: String, bucketName: String) = check(fileName.length > UUID_LENGTH)
        .let {
            minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(bucketName)
                    .`object`(fileName)
                    .build()
            ).readAllBytes()
        }

    fun upload(
        fileName: String, fileBytes: ByteArray, bucketName: String
    ) = ByteArrayInputStream(fileBytes).let {
        val uniqueFileName = makeUniqueFileName(fileName)
        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(bucketName)
                .stream(it, it.available().toLong(), -1)
                .`object`(uniqueFileName)
                .build()
        )
        uniqueFileName
    }

    private fun makeUniqueFileName(name: String) = name + UUID.randomUUID()
}
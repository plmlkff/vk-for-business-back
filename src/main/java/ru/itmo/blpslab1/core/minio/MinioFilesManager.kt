package ru.itmo.blpslab1.core.minio

import arrow.core.Either
import io.minio.*
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
        createBucketIfNotExist(bucketName)
        val uniqueFileName = makeUniqueFileName(fileName)
        ContinuableFileUploadTask(
            uniqueFileName = uniqueFileName,
            args = PutObjectArgs.builder()
                .bucket(bucketName)
                .stream(it, it.available().toLong(), -1)
                .`object`(uniqueFileName)
                .build()
        )
    }

    fun remove(
        fileName: String, bucketName: String
    ) = check(fileName.length > UUID_LENGTH).let {
        minioClient.removeObject(
            RemoveObjectArgs.builder()
                .bucket(bucketName)
                .`object`(fileName)
                .build()
        )
    }

    private fun makeUniqueFileName(name: String) = name + UUID.randomUUID()

    private fun createBucketIfNotExist(
        bucketName: String
    ) = try {
        val existRes = minioClient.bucketExists(
            BucketExistsArgs.builder()
                .bucket(bucketName)
                .build()
        )
        if (existRes) Unit
        else minioClient.makeBucket(
            MakeBucketArgs.builder()
                .bucket(bucketName)
                .build()
        )
    } catch (e: Exception) {
        throw RuntimeException()
    }

    inner class ContinuableFileUploadTask internal constructor(
        val uniqueFileName: String,
        val args: PutObjectArgs
    ) {
        fun `continue`() = Either.catch { minioClient.putObject(args) }
    }
}
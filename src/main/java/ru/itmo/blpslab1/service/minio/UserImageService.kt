package ru.itmo.blpslab1.service.minio

import arrow.core.Either

interface UserImageService {
    fun saveImage(name: String, bytes: ByteArray): Either<Throwable, String>

    fun getImage(name: String): Either<Throwable, ByteArray>
}
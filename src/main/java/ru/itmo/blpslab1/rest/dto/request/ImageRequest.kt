package ru.itmo.blpslab1.rest.dto.request

data class ImageRequest(
    val name: String,
    val bytes: ByteArray
)

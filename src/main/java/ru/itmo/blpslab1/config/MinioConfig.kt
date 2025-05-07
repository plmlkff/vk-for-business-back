package ru.itmo.blpslab1.config

import io.minio.MinioClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MinioConfig(
    @Value("\${minio.bucket}")
    val bucket: String,
    @Value("\${minio.url}")
    val url: String,
    @Value("\${minio.access-key}")
    val accessKey: String,
    @Value("\${minio.secret-key}")
    val secretKey: String
) {

    @Bean
    fun getMinioClient(): MinioClient{
        return MinioClient.builder()
            .endpoint(url)
            .credentials(accessKey, secretKey)
            .build();
    }

}
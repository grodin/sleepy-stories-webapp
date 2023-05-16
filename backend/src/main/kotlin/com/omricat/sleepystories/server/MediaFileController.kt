package com.omricat.sleepystories.server

import java.io.IOException
import java.nio.file.Path
import kotlin.jvm.Throws
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

public val MPEG_MEDIA_TYPE: MediaType = MediaType.parseMediaType("audio/mpeg")

public const val MEDIA_PATH: String = "/media"

@RestController
internal class MediaFileController(private val mediaFileService: MediaFileService) {

    @GetMapping("$MEDIA_PATH/{id}")
    @Throws(IOException::class)
    fun file(@PathVariable("id") id: MediaFileId): ResponseEntity<Resource> =
        mediaFileService[id].throwIfNull(id).let { path ->
            val resource = FileSystemResource(path)
            ResponseEntity.ok()
                .contentType(MPEG_MEDIA_TYPE)
                .contentLength(resource.contentLength())
                .body(resource)
        }
}

private fun Path?.throwIfNull(id: MediaFileId): Path =
    this ?: throw IOException("No file for id $id")

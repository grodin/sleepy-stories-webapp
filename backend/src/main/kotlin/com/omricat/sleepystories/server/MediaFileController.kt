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
import kotlin.io.path.extension

public val MPEG_MEDIA_TYPE: MediaType = MediaType.parseMediaType("audio/mpeg")
public val OGG_MEDIA_TYPE: MediaType = MediaType.parseMediaType("audio/ogg")

private val mediaTypeByExtension = mapOf(
    "mp3" to MPEG_MEDIA_TYPE,
    "ogg" to OGG_MEDIA_TYPE
)

public const val MEDIA_PATH: String = "/media"

@RestController
internal class MediaFileController(private val mediaFileService: MediaFileService) {

    @GetMapping("$MEDIA_PATH/{id}")
    @Throws(IOException::class)
    fun file(@PathVariable("id") id: MediaFileId): ResponseEntity<Resource> =
        mediaFileService[id].throwIfNull(id).let { path ->
            val mediaType = mediaTypeByExtension[path.extension]
                ?: return@let ResponseEntity.internalServerError().build()
            val resource = FileSystemResource(path)
            ResponseEntity.ok()
                .contentType(mediaType)
                .contentLength(resource.contentLength())
                .body(resource)
        }
}

private fun Path?.throwIfNull(id: MediaFileId): Path =
    this ?: throw IOException("No file for id $id")

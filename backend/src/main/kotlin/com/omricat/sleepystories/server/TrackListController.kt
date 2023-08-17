package com.omricat.sleepystories.server

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.DefaultUriBuilderFactory
import org.springframework.web.util.UriBuilderFactory
import java.net.URI

@RestController
internal class TrackListController(
    private val uriBuilderFactory: UriBuilderFactory = DefaultUriBuilderFactory(),
    private val mediaFileService: MediaFileService,
) {

    private fun mediaUri(id: MediaFileId): URI =
        uriBuilderFactory.builder().path(MEDIA_PATH).path("/${id.value}").build()

    @GetMapping("/api/track-list")
    fun trackList(): List<Track> =
        mediaFileService.asMap().map { (id, file) ->
            Track(id = id, url = mediaUri(id), mediaMetadata = file.mediaMetadata)
        }
}

internal data class Track(val id: MediaFileId, val url: URI, val mediaMetadata: MediaMetadata)

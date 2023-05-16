package com.omricat.sleepystories.server

import java.net.URI
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.DefaultUriBuilderFactory
import org.springframework.web.util.UriBuilderFactory

@RestController
internal class TrackListController(
    private val uriBuilderFactory: UriBuilderFactory = DefaultUriBuilderFactory(),
    private val mediaFileService: MediaFileService,
) {

    private fun mediaUri(id: MediaFileId): URI =
        uriBuilderFactory.builder().path(MEDIA_PATH).path("/${id.value}").build()

    @GetMapping("/api/track-list")
    fun trackList(): List<Track> =
        mediaFileService.ids().map { id -> Track(id = id, url = mediaUri(id)) }
}

internal data class Track(val id: MediaFileId, val url: URI)

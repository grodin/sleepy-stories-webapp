package com.omricat.sleepystories.server

import java.nio.file.Files
import java.nio.file.Path
import kotlin.streams.asSequence

public interface MediaFileService {

    public operator fun get(id: MediaFileId): Path?

    public fun asMap(): Map<MediaFileId, Path>

    public fun ids(): Set<MediaFileId>

    public companion object {
        public fun instance(basePath: Path): MediaFileService = DefaultMediaFileService(basePath)
    }

    public object NullMediaFileService: MediaFileService {
        override fun get(id: MediaFileId): Path? = null

        override fun asMap(): Map<MediaFileId, Path> = emptyMap()

        override fun ids(): Set<MediaFileId> = emptySet()
    }
}

private class DefaultMediaFileService(basePath: Path) : MediaFileService {

    private val matcher = basePath.fileSystem.getPathMatcher("glob:*.mp3")

    private val idToPath: Map<MediaFileId, Path> =
        Files.find(
                basePath,
                1,
                { path, fileAttributes ->
//                    matcher.matches(path) &&
                        fileAttributes.isRegularFile }
            )
            .parallel()
            .asSequence()
            .mapIndexed { i, path -> MediaFileId("$i") to path }
            .toMap()

    override fun get(id: MediaFileId): Path? = idToPath[id]

    override fun asMap(): Map<MediaFileId, Path> = HashMap(idToPath)

    override fun ids(): Set<MediaFileId> = idToPath.keys
}

@JvmInline public value class MediaFileId(public val value: String)

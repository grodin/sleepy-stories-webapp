package com.omricat.sleepystories.server

import com.google.common.hash.Hashing
import com.google.common.io.Files as GuavaFiles
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.io.path.isReadable
import kotlin.jvm.Throws
import kotlin.streams.asSequence

public interface MediaFileService {

    public operator fun get(id: MediaFileId): Path?

    public fun asMap(): Map<MediaFileId, Path>

    public fun ids(): Set<MediaFileId>

    public companion object {
        public fun instance(basePath: Path): MediaFileService =
            if (basePath.exists() && basePath.isReadable() && basePath.isDirectory()) {
                DefaultMediaFileService(basePath)
            } else {
                NullMediaFileService
            }
    }

    private object NullMediaFileService : MediaFileService {
        override fun get(id: MediaFileId): Path? = null

        override fun asMap(): Map<MediaFileId, Path> = emptyMap()

        override fun ids(): Set<MediaFileId> = emptySet()
    }
}

private class DefaultMediaFileService(basePath: Path) : MediaFileService {

    private val matcher = basePath.fileSystem.getPathMatcher("glob:*.{mp3,ogg}")

    private val idToPath: Map<MediaFileId, Path> =
        Files.find(
                basePath,
                1,
                { path, fileAttributes ->
                    matcher.matches(path.fileName) && fileAttributes.isRegularFile
                }
            )
            .use {
                it.parallel()
                    .asSequence()
                    .map { path -> MediaFileId.fromFile(path) to path }
                    .toMap()
            }

    override fun get(id: MediaFileId): Path? = idToPath[id]

    override fun asMap(): Map<MediaFileId, Path> = HashMap(idToPath)

    override fun ids(): Set<MediaFileId> = idToPath.keys
}

@JvmInline
public value class MediaFileId(public val value: String) {
    internal companion object {

        @Throws(IOException::class)
        fun fromFile(file: Path): MediaFileId =
            MediaFileId(GuavaFiles.asByteSource(file.toFile()).hash(Hashing.sha256()).toString())
    }
}

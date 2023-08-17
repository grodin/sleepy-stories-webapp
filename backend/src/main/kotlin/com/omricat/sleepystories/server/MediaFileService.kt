package com.omricat.sleepystories.server

import com.google.common.hash.Hashing
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.io.path.isReadable
import kotlin.streams.asSequence
import com.google.common.io.Files as GuavaFiles

public interface MediaFileService {

    public operator fun get(id: MediaFileId): MediaFile?

    public fun asMap(): Map<MediaFileId, MediaFile>

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
        override fun get(id: MediaFileId): MediaFile? = null

        override fun asMap(): Map<MediaFileId, MediaFile> = emptyMap()

        override fun ids(): Set<MediaFileId> = emptySet()
    }
}

private class DefaultMediaFileService(basePath: Path) : MediaFileService {

    private val matcher = basePath.fileSystem.getPathMatcher("glob:*.{mp3,ogg}")

    private val idToPath: Map<MediaFileId, MediaFile> =
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
                    .map { path -> MediaFileId.fromFile(path) to MediaFile.fromFile(path) }
                    .toMap()
            }

    override fun get(id: MediaFileId): MediaFile? = idToPath[id]

    override fun asMap(): Map<MediaFileId, MediaFile> = HashMap(idToPath)

    override fun ids(): Set<MediaFileId> = idToPath.keys
}

/** Represents a media file on disk */
public data class MediaFile(public val path: Path, public val mediaMetadata: MediaMetadata) {
    internal companion object {
        @Throws(IOException::class)
        fun fromFile(file: Path): MediaFile {
            val tags = AudioFileIO.read(file.toFile()).tag
            val meta =
                MediaMetadata(
                    title = tags.getFirst(FieldKey.TITLE),
                    artist = tags.getFirst(FieldKey.ARTIST),
                    album = tags.getFirst(FieldKey.ALBUM)
                )
            return MediaFile(path = file, mediaMetadata = meta)
        }
    }
}

@JvmInline
public value class MediaFileId(public val value: String) {
    internal companion object {

        @Throws(IOException::class)
        fun fromFile(file: Path): MediaFileId =
            MediaFileId(GuavaFiles.asByteSource(file.toFile()).hash(Hashing.sha256()).toString())
    }
}

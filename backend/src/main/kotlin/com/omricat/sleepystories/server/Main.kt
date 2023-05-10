package com.omricat.sleepystories.server

import java.nio.file.Path
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

public fun main(vararg args: String) {
    runApplication<SleepyStoriesServer>(*args)
}

private const val SLEEPY_STORIES_MEDIA_PATH = "SLEEPY_STORIES_MEDIA_PATH"

@SpringBootApplication
public class SleepyStoriesServer {

    @get:Bean
    public val mediaFileService: MediaFileService by lazy {
        val basePath = System.getenv()[SLEEPY_STORIES_MEDIA_PATH]?.let { Path.of(it) }
        if (basePath != null) {
            MediaFileService.instance(basePath)
        } else {
            MediaFileService.NullMediaFileService
        }
    }
}

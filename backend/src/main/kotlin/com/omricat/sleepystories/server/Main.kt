package com.omricat.sleepystories.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

public fun main(vararg args: String) {
    runApplication<SleepyStoriesServer>(*args)
}

@SpringBootApplication
public class SleepyStoriesServer

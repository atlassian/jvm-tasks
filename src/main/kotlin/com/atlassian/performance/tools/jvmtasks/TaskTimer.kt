package com.atlassian.performance.tools.jvmtasks

import org.apache.logging.log4j.CloseableThreadContext
import org.apache.logging.log4j.LogManager
import java.time.Duration
import java.time.Instant.now

object TaskTimer {

    private val logger = LogManager.getLogger(this::class.java)

    fun <T> time(
        label: String,
        task: () -> T
    ): T {
        CloseableThreadContext.push(label).use {
            val start = now()
            val result = task()
            val duration = Duration.between(start, now())

            logger.debug("The task took $duration")
            return result
        }
    }
}
package com.atlassian.performance.tools.jvmtasks.api

import org.apache.logging.log4j.CloseableThreadContext
import org.apache.logging.log4j.LogManager
import java.time.Duration
import java.time.Instant.now

object TaskTimer {

    private val logger = LogManager.getLogger(this::class.java)

    /**
     * Logs the duration taken by [task].
     */
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

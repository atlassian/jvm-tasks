package com.atlassian.performance.tools.jvmtasks.api

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

/**
 * @param description in imperative mood
 */
class IdempotentAction<out T>(
    private val description: String,
    private val action: () -> T
) {
    companion object {
        private val LOGGER: Logger = LogManager.getLogger(this::class.java)
    }

    /**
     * Retries [action] for at most [maxAttempts] using [backoff] between every attempt.
     */
    fun retry(
        maxAttempts: Int,
        backoff: Backoff
    ): T {
        val failedAttempts = mutableMapOf<Int, Exception>()
        var lastException: Exception? = null
        for (attempt in 1..maxAttempts) {
            try {
                return action()
            } catch (e: Exception) {
                lastException = e
                val delay = backoff.backOff(attempt)
                LOGGER.debug("Attempt #$attempt failed to $description, ${e.message}, backing off for $delay ...")
                failedAttempts += attempt to e
                Thread.sleep(delay.toMillis())
            }
        }
        failedAttempts.forEach { attempt, exception ->
            LOGGER.debug("Stacktrace for #$attempt failed attempt of $description", exception)
        }
        throw Exception("Failed to $description despite $maxAttempts attempts", lastException)
    }
}

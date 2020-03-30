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
        val attempts = 1..maxAttempts
        for (attempt in attempts) {
            try {
                return action()
            } catch (e: Exception) {
                lastException = e
                failedAttempts += attempt to e
                val failedAttemptMessage = "Attempt #$attempt failed to $description, ${e.message}"
                if (attempt == attempts.last) {
                    LOGGER.debug("$failedAttemptMessage, it was the last attempt")
                } else {
                    val delay = backoff.backOff(attempt)
                    LOGGER.debug("$failedAttemptMessage, backing off for $delay ...")
                    Thread.sleep(delay.toMillis())
                }
            }
        }
        failedAttempts.forEach { (attempt, exception) ->
            LOGGER.debug("Stacktrace for #$attempt failed attempt of $description", exception)
        }
        throw Exception("Failed to $description despite $maxAttempts attempts", lastException)
    }
}

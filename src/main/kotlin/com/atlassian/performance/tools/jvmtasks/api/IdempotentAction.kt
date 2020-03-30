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
        val attempts = 1..maxAttempts
        val failedAttempts = mutableListOf<FailedAttempt>()
        for (attempt in attempts) {
            try {
                return action()
            } catch (e: Exception) {
                val failedAttempt = FailedAttempt(attempt, e, description)
                failedAttempts += failedAttempt
                if (attempt != attempts.last) {
                    val delay = backoff.backOff(attempt)
                    LOGGER.debug("$failedAttempt, backing off for $delay ...")
                    Thread.sleep(delay.toMillis())
                }
            }
        }
        val lastAttempt = failedAttempts.last()
        LOGGER.debug("$lastAttempt, it was the last attempt")
        failedAttempts.forEach { LOGGER.debug("Stacktrace for $it", it.failure) }
        throw Exception("Failed to $description despite $maxAttempts attempts", lastAttempt.failure)

    }

    private class FailedAttempt(
        val attempt: Int,
        val failure: Exception,
        val action: String
    ) {
        override fun toString() = "Attempt #$attempt failed to $action, ${failure.message}"
    }
}

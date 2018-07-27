package com.atlassian.performance.tools.jvmtasks

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.time.Duration

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

    fun retry(
        maxAttempts: Int,
        backoff: Backoff
    ): T {
        val failedAttempts = mutableMapOf<Int, Exception>()
        for (attempt in 1..maxAttempts) {
            try {
                return action()
            } catch (e: Exception) {
                val delay = backoff.backOff(attempt)
                LOGGER.debug("Attempt #$attempt failed to $description, ${e.message}, backing off for $delay ...")
                failedAttempts += attempt to e
                Thread.sleep(delay.toMillis())
            }
        }
        failedAttempts.forEach { attempt, exception ->
            LOGGER.debug("Stacktrace for #$attempt failed attempt of $description", exception)
        }
        throw Exception("Failed to $description despite $maxAttempts attempts")
    }
}

interface Backoff {
    fun backOff(
        attempt: Int
    ): Duration
}

class ExponentialBackoff(
    private val baseBackoff: Duration,
    private val exponent: Double = 2.0
) : Backoff {

    override fun backOff(
        attempt: Int
    ): Duration {
        val factor = Math.pow(exponent, attempt.toDouble()).toLong()
        return baseBackoff.multipliedBy(factor)
    }
}
package com.atlassian.performance.tools.jvmtasks.api

import java.time.Duration
import java.util.Random
import kotlin.math.absoluteValue

/**
 * Backs off for a random duration, at most [max].
 *
 * @since 1.1.0
 */
class JitterBackoff(
    private val max: Duration
) : Backoff {

    private val random = Random()

    override fun backOff(attempt: Int): Duration {
        val randomMillis = random
            .nextLong()
            .absoluteValue
            .rem(max.toMillis())
        return Duration.ofMillis(randomMillis)
    }
}

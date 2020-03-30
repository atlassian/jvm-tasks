package com.atlassian.performance.tools.jvmtasks.api

import java.time.Duration

/**
 * Adds [a] and [b] together.
 * @since 1.1.0
 */
class SumBackoff(
    private val a: Backoff,
    private val b: Backoff
) : Backoff {

    override fun backOff(attempt: Int): Duration {
        return a.backOff(attempt) + b.backOff(attempt)
    }
}

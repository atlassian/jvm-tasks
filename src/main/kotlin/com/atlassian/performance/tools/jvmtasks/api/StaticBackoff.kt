package com.atlassian.performance.tools.jvmtasks.api

import java.time.Duration

/**
 * Backs off for the same [duration] every attempt.
 * @since 1.1.0
 */
class StaticBackoff(
    private val duration: Duration
) : Backoff {

    override fun backOff(attempt: Int): Duration = duration
}

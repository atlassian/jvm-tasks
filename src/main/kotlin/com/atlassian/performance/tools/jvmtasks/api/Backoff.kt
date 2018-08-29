package com.atlassian.performance.tools.jvmtasks.api

import java.time.Duration

/**
 * Calculates the waiting time for a given action attempt.
 */
interface Backoff {

    /**
     * @param attempt Counts action attempts.
     * @return Separates action attempts in time.
     */
    fun backOff(
        attempt: Int
    ): Duration
}

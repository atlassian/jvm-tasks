package com.atlassian.performance.tools.jvmtasks.api

import java.time.Duration

/**
 * Calculates the waiting time for a given action attempt.
 *
 * @since 1.0.0
 */
interface Backoff {

    /**
     * @param attempt Counts action attempts.
     * @return Separates action attempts in time.
     * @since 1.0.0
     */
    fun backOff(
        attempt: Int
    ): Duration
}

/**
 * @param other Prolongs this back-off.
 * @return Sums both back-offs.
 * @since 1.1.0
 */
operator fun Backoff.plus(
    other: Backoff
): Backoff = SumBackoff(this, other)

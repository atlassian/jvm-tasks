package com.atlassian.performance.tools.jvmtasks.api

import java.time.Duration

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

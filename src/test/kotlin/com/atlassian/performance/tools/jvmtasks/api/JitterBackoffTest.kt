package com.atlassian.performance.tools.jvmtasks.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.time.Duration.ofMillis
import java.time.Duration.ofSeconds
import java.util.Random

class JitterBackoffTest {

    @Test
    fun shouldYieldPositiveTime() {
        val backoff = JitterBackoff(ofSeconds(10), Random(888))

        val durations = (1..1000).map { backoff.backOff(1) }

        assertThat(durations).noneMatch { it.isNegative }
    }

    @Test
    fun shouldSpread() {
        val backoff = JitterBackoff(ofSeconds(10), Random(555))

        val durations = (1..1000).map { backoff.backOff(1) }

        val spread = durations.max()!! - durations.min()!!
        assertThat(spread).isGreaterThan(ofSeconds(9) + ofMillis(500))
    }
}

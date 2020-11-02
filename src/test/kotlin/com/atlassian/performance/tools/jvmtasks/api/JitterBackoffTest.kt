package com.atlassian.performance.tools.jvmtasks.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.time.Duration.ofSeconds

class JitterBackoffTest {

    @Test
    fun shouldYieldPositiveTime() {
        val backoff = JitterBackoff(ofSeconds(10))

        val durations = (1..1000).map { backoff.backOff(1) }

        assertThat(durations).noneMatch { it.isNegative }
    }
}

package org.secureline.app.domain.retention

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class RetentionEngineTest {
    @Test
    fun timed_expiresWithFakeClock() {
        var now = 1_000L
        val engine =
            RetentionEngine(
                config = RetentionConfig(mode = RetentionMode.TIMED, timedTtlMillis = 100),
                clock = { now },
            )
        engine.store("m1", createdAtMillis = now)
        assertThat(engine.size()).isEqualTo(1)
        now = 1_050L
        assertThat(engine.purgeExpired()).isEqualTo(0)
        now = 1_200L
        assertThat(engine.purgeExpired()).isEqualTo(1)
        assertThat(engine.get("m1")).isNull()
    }

    @Test
    fun ephemeral_refusesDurableStoreAndWipesSession() {
        val engine = RetentionEngine(RetentionConfig(mode = RetentionMode.EPHEMERAL))
        assertThat(engine.store("x")).isNull()
        // Simulate in-session buffer tracking then wipe
        val persistent =
            RetentionEngine(RetentionConfig(mode = RetentionMode.PERSISTENT)).also {
                it.store("s1")
            }
        persistent.markSessionBuffersWiped(listOf("s1"))
        assertThat(persistent.get("s1")!!.wiped).isTrue()
    }

    @Test
    fun persistent_keepsUntilDeleted() {
        val engine = RetentionEngine(RetentionConfig(mode = RetentionMode.PERSISTENT))
        engine.store("a")
        assertThat(engine.purgeExpired(Long.MAX_VALUE)).isEqualTo(0)
        assertThat(engine.size()).isEqualTo(1)
    }
}

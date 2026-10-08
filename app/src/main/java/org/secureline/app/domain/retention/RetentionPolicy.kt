package org.secureline.app.domain.retention

enum class RetentionMode {
    PERSISTENT,
    TIMED,
    EPHEMERAL,
}

data class RetentionConfig(
    val mode: RetentionMode = RetentionMode.PERSISTENT,
    /** Used when [mode] is [RetentionMode.TIMED]; zero or negative means immediate expiry. */
    val timedTtlMillis: Long = 24L * 60L * 60L * 1000L,
)

data class RetainedItem(
    val id: String,
    val createdAtMillis: Long,
    var wiped: Boolean = false,
)

/**
 * Pure retention rules with injectable clock for tests.
 */
class RetentionEngine(
    private val config: RetentionConfig,
    private val clock: () -> Long = { System.currentTimeMillis() },
) {
    private val items = LinkedHashMap<String, RetainedItem>()

    fun store(
        id: String,
        createdAtMillis: Long = clock(),
    ): RetainedItem? {
        if (config.mode == RetentionMode.EPHEMERAL) {
            // Ephemeral: caller may hold in memory only; engine refuses durable store.
            return null
        }
        val item = RetainedItem(id = id, createdAtMillis = createdAtMillis)
        items[id] = item
        return item
    }

    fun wipeEphemeralSession(sessionIds: Collection<String>) {
        if (config.mode != RetentionMode.EPHEMERAL) return
        for (id in sessionIds) {
            items.remove(id)
        }
    }

    fun markSessionBuffersWiped(sessionIds: Collection<String>) {
        for (id in sessionIds) {
            items[id]?.wiped = true
            if (config.mode == RetentionMode.EPHEMERAL) {
                items.remove(id)
            }
        }
    }

    fun purgeExpired(nowMillis: Long = clock()): Int {
        if (config.mode != RetentionMode.TIMED) return 0
        val ttl = config.timedTtlMillis
        val expired = items.values.filter { nowMillis - it.createdAtMillis >= ttl }
        expired.forEach { items.remove(it.id) }
        return expired.size
    }

    fun get(id: String): RetainedItem? = items[id]

    fun size(): Int = items.size
}

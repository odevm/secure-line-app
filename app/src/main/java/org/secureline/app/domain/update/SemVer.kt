package org.secureline.app.domain.update

/**
 * Strict SemVer core: MAJOR.MINOR.PATCH (optional leading `v` on tags).
 */
data class SemVer(
    val major: Int,
    val minor: Int,
    val patch: Int,
) : Comparable<SemVer> {
    override fun compareTo(other: SemVer): Int =
        compareValuesBy(this, other, SemVer::major, SemVer::minor, SemVer::patch)

    override fun toString(): String = "$major.$minor.$patch"

    companion object {
        private val PATTERN = Regex("^v?(\\d+)\\.(\\d+)\\.(\\d+)$")

        fun parse(raw: String): SemVer? {
            val m = PATTERN.matchEntire(raw.trim()) ?: return null
            return SemVer(
                major = m.groupValues[1].toInt(),
                minor = m.groupValues[2].toInt(),
                patch = m.groupValues[3].toInt(),
            )
        }
    }
}

object UpdateChecker {
    fun isNewer(
        remoteTag: String,
        localVersionName: String,
    ): Boolean {
        val remote = SemVer.parse(remoteTag) ?: return false
        val local = SemVer.parse(localVersionName) ?: return false
        return remote > local
    }
}

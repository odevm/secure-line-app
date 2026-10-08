package org.secureline.app.domain.firebase

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class FcmPayloadContractTest {
    @Test
    fun consoleTitleBody_allowed() {
        assertThat(
            FcmPayloadContract.isAllowed(
                mapOf("title" to "Update available", "body" to "See GitHub Releases"),
            ),
        ).isTrue()
    }

    @Test
    fun sensitiveKeys_rejected() {
        assertThat(
            FcmPayloadContract.isAllowed(mapOf("session_key" to "abc")),
        ).isFalse()
        assertThat(
            FcmPayloadContract.isAllowed(mapOf("note" to "contains plaintext leak")),
        ).isFalse()
    }
}

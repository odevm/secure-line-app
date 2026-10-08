package org.secureline.app.domain.backup

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.security.GeneralSecurityException

class BackupEnvelopeTest {
    @Test
    fun roundTrip() {
        val blob = BackupEnvelope.encrypt("passphrase-ok", "backup-bytes".toByteArray())
        assertThat(String(BackupEnvelope.decrypt("passphrase-ok", blob))).isEqualTo("backup-bytes")
    }

    @Test
    fun wrongPassphrase_fails() {
        val blob = BackupEnvelope.encrypt("passphrase-ok", byteArrayOf(1, 2, 3))
        assertThrows(GeneralSecurityException::class.java) {
            BackupEnvelope.decrypt("wrong-pass", blob)
        }
    }
}

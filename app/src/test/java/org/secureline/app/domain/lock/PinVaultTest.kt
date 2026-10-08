package org.secureline.app.domain.lock

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.security.GeneralSecurityException

class PinVaultTest {
    @Test
    fun wrapUnwrap_roundTrip() {
        val dbKey = PinVault.generateDbKey()
        val wrapped = PinVault.wrap("123456", dbKey)
        assertThat(PinVault.unwrap("123456", wrapped)).isEqualTo(dbKey)
    }

    @Test
    fun wrongPin_fails() {
        val wrapped = PinVault.wrap("123456", PinVault.generateDbKey())
        assertThrows(GeneralSecurityException::class.java) {
            PinVault.unwrap("000000", wrapped)
        }
    }
}

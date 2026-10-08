package org.secureline.app.domain.contacts

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class ContactBookTest {
    private fun contact(
        id: String,
        numbers: List<String>,
        fp: String = "FP1",
    ) = Contact(
        peerLocalUserId = id,
        x25519PublicKey = ByteArray(32) { 1 },
        ed25519PublicKey = ByteArray(32) { 2 },
        keyFingerprint = fp,
        phoneNumbers = numbers,
    )

    @Test
    fun mergeSameId_appendsNumberWithoutDuplicateIdentity() {
        val book = ContactBook()
        book.upsert(contact("peer-1", listOf("+111")))
        book.upsert(contact("peer-1", listOf("+222")))
        book.upsert(contact("peer-1", listOf("+111")))
        assertThat(book.all()).hasSize(1)
        assertThat(book.get("peer-1")!!.phoneNumbers).containsExactly("+111", "+222").inOrder()
        assertThat(book.findByPhone("+222")!!.peerLocalUserId).isEqualTo("peer-1")
    }

    @Test
    fun differentIds_remainSeparate() {
        val book = ContactBook()
        book.upsert(contact("a", listOf("+1")))
        book.upsert(contact("b", listOf("+2")))
        assertThat(book.all()).hasSize(2)
    }
}

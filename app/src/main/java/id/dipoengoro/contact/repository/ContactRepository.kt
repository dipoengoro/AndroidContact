package id.dipoengoro.contact.repository

import id.dipoengoro.contact.db.Contact
import id.dipoengoro.contact.db.ContactDatabase

class ContactRepository(private val db: ContactDatabase) {
    suspend fun insertContact(contact: Contact) = db.contactDao().insertContact(contact)
    suspend fun deleteContact(contact: Contact) = db.contactDao().deleteContact(contact)
    suspend fun updateContact(contact: Contact) = db.contactDao().updateContact(contact)
    fun getAllNotes() = db.contactDao().getAllContacts()
}
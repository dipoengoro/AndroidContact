package id.dipoengoro.contact.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.dipoengoro.contact.db.Contact
import id.dipoengoro.contact.repository.ContactRepository
import kotlinx.coroutines.launch

class ContactViewModel(private val repository: ContactRepository) : ViewModel() {
    fun addContact(contact: Contact) = viewModelScope.launch {
        repository.insertContact(contact)
    }

    fun deleteContact(contact: Contact) = viewModelScope.launch {
        repository.deleteContact(contact)
    }

    fun updateContact(contact: Contact) = viewModelScope.launch {
        repository.updateContact(contact)
    }

    fun getAllContacts() = repository.getAllNotes()
}
package id.dipoengoro.contact.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.dipoengoro.contact.repository.ContactRepository

class ContactViewModelFactory(
    private val repository: ContactRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactViewModel::class.java))
            return ContactViewModel(repository) as T
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
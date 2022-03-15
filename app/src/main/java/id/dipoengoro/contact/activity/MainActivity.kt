package id.dipoengoro.contact.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.dipoengoro.contact.R
import id.dipoengoro.contact.adapter.RecyclerAdapter
import id.dipoengoro.contact.databinding.ActivityMainBinding
import id.dipoengoro.contact.db.Contact
import id.dipoengoro.contact.db.ContactDatabase
import id.dipoengoro.contact.repository.ContactRepository
import id.dipoengoro.contact.viewmodel.ContactViewModel
import id.dipoengoro.contact.viewmodel.ContactViewModelFactory
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class MainActivity : AppCompatActivity() {
    private lateinit var adapter: RecyclerAdapter

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: ContactViewModel
    private lateinit var selectedContact: Contact
    private var selectedPosition: Int = 0
    private var isListClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val repository = ContactRepository(ContactDatabase.getInstance(this))
        val factory = ContactViewModelFactory(repository)
        viewModel = ViewModelProvider(this@MainActivity, factory)[ContactViewModel::class.java]
        adapter = RecyclerAdapter({ contact, _ ->
            isListClicked = !isListClicked
            listItemClicked(contact)
            Log.i("MyTag", isListClicked.toString())
        }, { contact, _ ->
            deleteContact(contact)
        }, {
            intentChat(it)
        })

        binding.apply {
            recycler.layoutManager = LinearLayoutManager(this@MainActivity)
            recycler.adapter = adapter

            viewModel.getAllContacts().observe(this@MainActivity) {
                adapter.setList(it)
                adapter.notifyDataSetChanged()
            }

            buttonSave.setOnClickListener {
                if (isListClicked) {
                    updateContact()
                } else {
                    saveContact()
                }
                if (isListClicked) {
                    isListClicked = false
                    buttonSave.text = getString(R.string.save)
                }
                editName.text.clear()
                editNumber.text.clear()
                Log.i("MyTag", isListClicked.toString())
                listItemClicked()
            }
        }
    }

    private fun getContactEdit(): Contact {
        binding.apply {
            val name = editName.text.toString()
            val phone = editNumber.text.toString()
            val phoneWithRegion: String = if (phone.startsWith("0")) {
                phone.replaceFirstChar { "+62" }
            } else {
                phone
            }
            return Contact(0, name, phone, phoneWithRegion)
        }
    }

    private fun contactToEdit(contact: Contact): Contact {
        binding.apply {
            editName.setText(contact.name)
            editNumber.setText(contact.phone)
            return contact
        }
    }

    private fun saveContact() {
        val contact = getContactEdit()
        viewModel.addContact(contact)
        Log.i("MyTag", "${contact.name} ${contact.phone}")
    }

    private fun updateContact() {
        val getData = getContactEdit()
        val updateContact = Contact(selectedContact.id, getData.name, getData.phone, getData.phoneWRegion)
        viewModel.updateContact(updateContact)
        Log.i("MyTag", "${updateContact.name} ${updateContact.phone}")
    }

    private fun deleteContact(contact: Contact) = viewModel.deleteContact(contact)

    private fun listItemClicked(contact: Contact? = null) =
        binding.apply {
            if (isListClicked && contact != null) {
                contactToEdit(contact = contact)
                selectedContact = contact
                buttonSave.text = getString(R.string.update)
            } else {
                editName.text.clear()
                editNumber.text.clear()
                buttonSave.text = getString(R.string.save)
            }
        }

    private fun intentChat(contact: Contact) {
        val phone = contact.phoneWRegion.replaceFirstChar { "" }
        try {
            customIntent("whatsapp://send?phone=$phone")
        } catch (e: Exception) {
            e.printStackTrace()
            val appPackageName = "com.whatsapp"
            try {
                customIntent("market://details?id=$appPackageName")
            } catch (e: android.content.ActivityNotFoundException) {
                customIntent("https://play.google.com/store/apps/details?id=$appPackageName")
            }
        }
    }

    private fun customIntent(uriString: String) {
        startActivity(Intent(Intent.ACTION_VIEW,
            Uri.parse(uriString)))
    }
}
package id.dipoengoro.contact.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import id.dipoengoro.contact.R
import id.dipoengoro.contact.databinding.FragmentAddEditBinding
import id.dipoengoro.contact.db.Contact
import id.dipoengoro.contact.db.ContactDatabase
import id.dipoengoro.contact.repository.ContactRepository
import id.dipoengoro.contact.viewmodel.ContactViewModel
import id.dipoengoro.contact.viewmodel.ContactViewModelFactory
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class AddEditFragment : Fragment() {
    private lateinit var binding: FragmentAddEditBinding
    private val args: AddEditFragmentArgs by navArgs()
    private var selectedContact = 0
    private lateinit var viewModel: ContactViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddEditBinding.inflate(inflater, container, false)
        val repository = ContactRepository(ContactDatabase.getInstance(requireContext()))
        val factory = ContactViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ContactViewModel::class.java]
        binding.apply {
            when (args.contact) {
                null -> buttonSave.setOnClickListener {
                    addContact()
                }
                else -> args.contact?.let {
                    it.apply {
                        selectedContact = id
                        editName.setText(name)
                        editNumber.setText(phone)
                    }
                    buttonSave.text = getString(R.string.update)
                    buttonSave.setOnClickListener {
                        editContact()
                    }
                }
            }
        }
        return binding.root
    }

    private fun getContactEdit(id: Int = 0): Contact {
        binding.apply {
            val name = editName.text.trim().toString()
            val phone = editNumber.text.trim().toString()
            val phoneWithRegion: String = if (phone.startsWith("0")) {
                phone.replaceFirstChar { "+62" }
            } else {
                phone
            }
            return Contact(id, name, phone, phoneWithRegion)
        }
    }

    private fun addContact() = getContactEdit().apply {
        if (name == "" && phone == "") {
            Toast.makeText(requireContext(), "Please fill all blank form", Toast.LENGTH_SHORT)
                .show()
        } else if (name == "") {
            Toast.makeText(requireContext(), "Please fill name form", Toast.LENGTH_SHORT).show()
        } else if (phone == "") {
            Toast.makeText(requireContext(), "Please fill phone form", Toast.LENGTH_SHORT)
                .show()
        } else {
            viewModel.addContact(this)
            requireActivity().onBackPressed()
        }
    }

    private fun editContact() = getContactEdit(selectedContact).apply {
        if (name == "" && phone == "") {
            Toast.makeText(requireContext(), "Please fill all blank form", Toast.LENGTH_SHORT)
                .show()
        } else if (name == "") {
            Toast.makeText(requireContext(), "Please fill name form", Toast.LENGTH_SHORT).show()
        } else if (phone == "") {
            Toast.makeText(requireContext(), "Please fill phone form", Toast.LENGTH_SHORT)
                .show()
        } else {
            viewModel.updateContact(this)
            requireActivity().onBackPressed()
        }
    }
}

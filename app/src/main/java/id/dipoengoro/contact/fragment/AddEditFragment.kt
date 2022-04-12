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
                    validated()
                }
                else -> args.contact?.let {
                    it.apply {
                        selectedContact = id
                        editName.setText(name)
                        editNumber.setText(phone)
                    }
                    buttonSave.text = getString(R.string.update)
                    buttonSave.setOnClickListener {
                        validated()
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

    private fun validated() = getContactEdit(selectedContact).apply {
        if (name == "" && phone == "") {
            toasting(getString(R.string.all_blank))
        } else if (name == "") {
            toasting(getString(R.string.name_blank))
        } else if (phone == "") {
            toasting(getString(R.string.phone_blank))
        } else {
            if (this.id == 0) {
                viewModel.addContact(this)
            } else {
                viewModel.updateContact(this)
            }
            requireActivity().onBackPressed()
        }
    }

    private fun toasting(message: String) =
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

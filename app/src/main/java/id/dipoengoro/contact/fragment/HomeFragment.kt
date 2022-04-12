package id.dipoengoro.contact.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import id.dipoengoro.contact.R
import id.dipoengoro.contact.adapter.RecyclerAdapter
import id.dipoengoro.contact.databinding.FragmentHomeBinding
import id.dipoengoro.contact.db.Contact
import id.dipoengoro.contact.db.ContactDatabase
import id.dipoengoro.contact.repository.ContactRepository
import id.dipoengoro.contact.viewmodel.ContactViewModel
import id.dipoengoro.contact.viewmodel.ContactViewModelFactory
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this,
            // factory
            ContactViewModelFactory(
                // repository
                ContactRepository(
                    ContactDatabase.getInstance(requireContext())
                )
            )
        )[ContactViewModel::class.java]
        val adapter = RecyclerAdapter({ contact, view ->
            view.findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToAddEditFragment(
                    label = getString(R.string.edit_fragment),
                    contact = contact
                )
            )
        }, { viewModel.deleteContact(it) }, { intentChat(it) })

        viewModel.getAllContacts().observe(viewLifecycleOwner) {
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        }
        binding.apply {
            recycler.layoutManager = LinearLayoutManager(requireContext())
            recycler.adapter = adapter
            fabAdd.setOnClickListener {
                it.findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToAddEditFragment(
                        label = getString(R.string.add_fragment)
                    )
                )
            }
        }
        return binding.root
    }

    private fun intentChat(contact: Contact) = contact.phoneWRegion.replaceFirstChar { "" }.apply {
        try {
            customIntent("whatsapp://send?phone=$this")
        } catch (e: Exception) {
            e.printStackTrace()
            "com.whatsapp".apply {
                try {
                    customIntent("market://details?id=$this@apply")
                } catch (e: android.content.ActivityNotFoundException) {
                    customIntent("https://play.google.com/store/apps/details?id=$this@apply")
                }
            }
        }
    }

    private fun customIntent(uriString: String) =
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(uriString)
            )
        )
}
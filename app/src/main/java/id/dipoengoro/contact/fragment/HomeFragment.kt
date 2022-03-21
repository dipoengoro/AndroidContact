package id.dipoengoro.contact.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import id.dipoengoro.contact.adapter.RecyclerAdapter
import id.dipoengoro.contact.databinding.FragmentHomeBinding
import id.dipoengoro.contact.db.ContactDatabase
import id.dipoengoro.contact.repository.ContactRepository
import id.dipoengoro.contact.viewmodel.ContactViewModel
import id.dipoengoro.contact.viewmodel.ContactViewModelFactory
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: ContactViewModel
    private lateinit var adapter: RecyclerAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val repository = ContactRepository(ContactDatabase.getInstance(requireContext()))
        val factory = ContactViewModelFactory(repository)
        viewModel = ViewModelProvider(requireActivity(), factory)[ContactViewModel::class.java]
        adapter = RecyclerAdapter({ contact, _ ->
            view?.findNavController()?.navigate(
                HomeFragmentDirections.actionHomeFragmentToAddEditFragment(
                    label = "Edit Contact",
                    contact = contact
                )
            )
        }, { _, _ ->

        }, {

        })
        binding.apply {
            recycler.layoutManager = LinearLayoutManager(requireActivity())
            recycler.adapter = adapter
            viewModel.getAllContacts().observe(requireActivity()) {
                adapter.setList(it)
                adapter.notifyDataSetChanged()
            }
            fabAdd.setOnClickListener {
                it.findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToAddEditFragment()
                )
            }
        }
        return binding.root
    }
}
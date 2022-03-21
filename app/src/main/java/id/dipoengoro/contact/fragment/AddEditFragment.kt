package id.dipoengoro.contact.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import id.dipoengoro.contact.R
import id.dipoengoro.contact.databinding.FragmentAddEditBinding

class AddEditFragment : Fragment() {
    private lateinit var binding: FragmentAddEditBinding
    private val args: AddEditFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddEditBinding.inflate(inflater, container, false)

        binding.apply {
            if (args.contact != null) {
                val contact = args.contact!!
                editName.setText(contact.name)
                editNumber.setText(contact.phone)
            }
        }
        return binding.root
    }
}
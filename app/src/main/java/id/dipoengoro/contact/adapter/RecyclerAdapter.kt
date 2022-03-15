package id.dipoengoro.contact.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.dipoengoro.contact.databinding.ListItemBinding
import id.dipoengoro.contact.db.Contact

class RecyclerAdapter(
    private val onItemClicked: (Contact, position: Int) -> Unit,
    private val onDeleteClicked: (Contact, position: Int) -> Unit,
    private val onChatClicked: (Contact) -> Unit
) : RecyclerView.Adapter<ViewHolder>() {

    private val contactList = ArrayList<Contact>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(
            contactList[position],
            { onItemClicked(it, position) },
            { onDeleteClicked(it, position) },
            { onChatClicked(it) }
        )

    override fun getItemCount(): Int = contactList.size

    fun setList(contacts: List<Contact>) {
        contactList.clear()
        contactList.addAll(contacts)
    }
}

class ViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        contact: Contact,
        onItemClicked: (Contact) -> Unit,
        onDeleteClicked: (Contact) -> Unit,
        onChatClicked: (Contact) -> Unit
    ) {
        binding.apply {
            textName.text = contact.name
            textNumber.text = contact.phone
            this.root.setOnClickListener {
                onItemClicked(contact)
            }
            iconDelete.setOnClickListener {
                onDeleteClicked(contact)
            }
            iconWhatsApp.setOnClickListener {
                onChatClicked(contact)
            }
        }
    }
}
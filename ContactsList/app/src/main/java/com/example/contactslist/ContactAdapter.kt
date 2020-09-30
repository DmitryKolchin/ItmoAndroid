package com.example.contactslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.contacts_list_item.view.*


class ContactAdapter(
    private val contacts : List<Contact>,
    private val onClick : (Contact) -> Unit
): RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    class ContactViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
        fun bind(contact: Contact){
            with(root){
                contact_name.text = contact.name
                phone_number.text = contact.phoneNumber
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContactViewHolder {
        val holder = ContactViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.contacts_list_item, parent, false)
        )
        holder.root.setOnClickListener{
            onClick(contacts[holder.adapterPosition])}
        return holder;

    }

    override fun getItemCount() = this.contacts.size

    override fun onBindViewHolder(holder: ContactAdapter.ContactViewHolder, position: Int) = holder.bind(contacts[position])

}
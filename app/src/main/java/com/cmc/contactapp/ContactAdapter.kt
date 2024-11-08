package com.cmc.contactapp

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.cmc.contactapp.databinding.ContactItemBinding
import kotlin.random.Random

class ContactAdapter(private val context: Context) :
    RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {
    private var contacts: List<Contact> = listOf()

    inner class ContactViewHolder(val binding: ContactItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ContactItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        holder.binding.contactName.text = contact.name
        holder.binding.contactPhoneNumber.text = contact.phoneNumber

        val firstChar = contact.name.firstOrNull()
        if (firstChar != null && firstChar.isLetter()) {
            holder.binding.circleImageView.text = firstChar.toString()
            val drawable = context.getDrawable(R.drawable.circle) as GradientDrawable
            drawable.setColor(getRandomColor()) // Set random color
            holder.binding.circleImageView.background = drawable
        } else {
            holder.binding.circleImageView.background = context.getDrawable(R.drawable.user)
            holder.binding.circleImageView.text = ""
        }

        holder.binding.optionsMenu.setOnClickListener {
            showPopupMenu(holder.binding.optionsMenu, contact.phoneNumber)
        }
    }

    override fun getItemCount() = contacts.size

    fun setContacts(contacts: List<Contact>) {
        this.contacts = contacts
        notifyDataSetChanged()
    }

    private fun showPopupMenu(view: View, phoneNumber: String) {
        val popup = PopupMenu(
            context,
            view
        )
        popup.inflate(R.menu.menu_item)
        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.call -> {
                    val callIntent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:$phoneNumber")
                    }
                    context.startActivity(callIntent)
                }

                R.id.sms -> {
                    val smsIntent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("smsto:$phoneNumber")
                    }
                    context.startActivity(smsIntent)
                }
            }
            true
        }
        popup.show()
    }

    private fun getRandomColor(): Int {
        val random = Random.Default
        val baseColor = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256))
        return baseColor
    }
}

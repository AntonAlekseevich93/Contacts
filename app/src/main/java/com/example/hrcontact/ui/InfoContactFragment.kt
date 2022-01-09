package com.example.hrcontact.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.contacts.R

class InfoContactFragment(val number: String, val name: String, val description: String) :
    DialogFragment() {


    companion object {
        const val TAG: String = "InfoContactFragment"
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater: LayoutInflater = requireActivity().layoutInflater
        val view: View = inflater.inflate(R.layout.info_contact_layout, null)
        val tvNameContact: TextView = view.findViewById(R.id.infoContactTvNumber)
        val tvDescription: TextView = view.findViewById(R.id.infoContactTvDescription)

        tvNameContact.text = name
        if (description.isNotEmpty()
            && !description.contentEquals("\n")
        )
            tvDescription.text = description
        else tvDescription.text =
            requireContext().resources.getString(R.string.description_is_missing)

        val buttonCall: ImageView = view.findViewById(R.id.imgvCall)
        buttonCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null))
            if (intent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(intent)
            }
        }

        return AlertDialog.Builder(requireContext())
            .setView(view)
            .setMessage(requireContext().resources.getString(R.string.info_about_contact))
            .create()


    }
}
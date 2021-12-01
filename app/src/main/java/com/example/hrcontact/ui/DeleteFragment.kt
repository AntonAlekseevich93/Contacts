package com.example.hrcontact.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.contacts.R
import com.example.hrcontact.support.ActionEnum
import com.example.hrcontact.support.IClickListenerAdapterContact
import com.example.hrcontact.support.IClickListenerDismiss
import com.example.hrcontact.viewmodel.ContactViewModel


class DeleteFragment() : DialogFragment() {

    private lateinit var mContext: Context
    private var type: Int = -1;
    private lateinit var actionEnum: ActionEnum
    private var idDelete: Int = -1;
    private lateinit var viewModel: ContactViewModel

     lateinit var iClickListenerDismiss: IClickListenerDismiss
    private lateinit var iClickListenerAdapterContact: IClickListenerAdapterContact


    companion object {
        const val TAG: String = "deleteFragment"
        const val TAG_ACTION: String = "TAG_DELETE_ACTION"
        const val TAG_ID: String = "TAG_DELETE_ID";
        const val TAG_TYPE: String = "TAG_DELETE_TYPE";
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        when {
            requireParentFragment() is IClickListenerDismiss -> {
                iClickListenerDismiss = requireParentFragment() as IClickListenerDismiss
            }
            requireParentFragment() is IClickListenerAdapterContact -> {
                iClickListenerAdapterContact =
                    requireParentFragment() as IClickListenerAdapterContact
            }
            else -> throw RuntimeException(
                requireParentFragment().toString()
                        + " must implement IClickListenerDismiss"
            )
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type = arguments?.getInt(TAG_TYPE) ?: -1
        idDelete = arguments?.getInt(TAG_ID) ?: -1
        actionEnum = (arguments?.getSerializable(TAG_ACTION) as ActionEnum?)!!
        viewModel = ViewModelProvider(requireActivity())[ContactViewModel::class.java]
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var name = when (type) {
            0 -> mContext.getString(R.string.type_contact)
            1 -> mContext.getString(R.string.delete_type_group)
            else -> mContext.getString(R.string.type_subgroup)
        }

        return AlertDialog.Builder(requireContext())
            .setMessage(
                requireContext().resources.getString(R.string.confirmation_of_group_deletion)
                        + " " + name
            )
            .setPositiveButton(
                requireContext().resources.getString(R.string.positiveButtonDialogFragment)
            ) { p0, p1 ->
                viewModel.delete(actionEnum, idDelete)
                parentFragmentManager.popBackStack()
                if (actionEnum == ActionEnum.DELETE_CONTACT) iClickListenerAdapterContact.update()
                else iClickListenerDismiss.startDismissWithUpdateData()
            }
            .setNegativeButton(
                requireContext().resources.getString(R.string.negativeButtonDialogFragment)
            ) { p0, p1 -> parentFragmentManager.popBackStack() }
            .create()

    }

}
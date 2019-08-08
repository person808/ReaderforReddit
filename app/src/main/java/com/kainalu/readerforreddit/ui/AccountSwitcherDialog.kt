package com.kainalu.readerforreddit.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kainalu.readerforreddit.R

class AccountSwitcherDialog : BottomSheetDialogFragment() {

    private val args by navArgs<AccountSwitcherDialogArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.account_switcher_dialog, container, false)
        val linearLayout = layout.findViewById<LinearLayout>(R.id.linearLayout)
        args.users.forEach { user ->
            val view = AccountSwitcherItemView(requireContext()).apply {
                setUserData(user)
                isChecked = user == args.currentUser
            }
            linearLayout.addView(view)
        }
        linearLayout.addView(AddAccountItemView(requireContext()))
        return layout
    }
}
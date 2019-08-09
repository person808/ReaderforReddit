package com.kainalu.readerforreddit.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kainalu.readerforreddit.AccountSwitcherViewModel
import com.kainalu.readerforreddit.R
import com.kainalu.readerforreddit.di.Injector
import com.kainalu.readerforreddit.di.ViewModelFactory
import com.kainalu.readerforreddit.user.UserData
import javax.inject.Inject

class AccountSwitcherDialog : BottomSheetDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by viewModels<AccountSwitcherViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Injector.get().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.account_switcher_dialog, container, false)
        val linearLayout = layout.findViewById<LinearLayout>(R.id.linearLayout)
        viewModel.userLiveData.observe(viewLifecycleOwner, object : Observer<Pair<UserData, List<UserData>>> {
            override fun onChanged(it: Pair<UserData, List<UserData>>) {
                val (currentUser, users) = it
                users.forEach { user ->
                    val view = AccountSwitcherItemView(requireContext()).apply {
                        setUserData(user)
                        isChecked = user == currentUser
                    }
                    linearLayout.addView(view)
                }

                val addAccount = AddAccountItemView(requireContext()).apply {
                    setOnClickListener {
                        findNavController().navigate(AccountSwitcherDialogDirections.actionAccountSwitcherDialogToAuthFragment())
                    }
                }
                linearLayout.addView(addAccount)
                viewModel.userLiveData.removeObserver(this)
            }
        })

        return layout
    }
}
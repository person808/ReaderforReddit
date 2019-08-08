package com.kainalu.readerforreddit

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.kainalu.readerforreddit.di.Injector
import com.kainalu.readerforreddit.di.ViewModelFactory
import com.kainalu.readerforreddit.ui.AccountSwitcherDialogDirections
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by viewModels<ActivityViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Injector.get().inject(this)

        val navController = findNavController(R.id.navHostFragment)
        bottomNavigation.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.submissionFragment) {
                bottomNavigation.visibility = View.GONE
            } else {
                bottomNavigation.visibility = View.VISIBLE
            }
        }

        // We have to use a layout change listener to override the long click of the profile view
        bottomNavigation.addOnLayoutChangeListener { v, _, _, _, _, _, _, _, _ ->
            val view = v.rootView.findViewById<View>(R.id.profile)
            view.setOnLongClickListener {
                val action = AccountSwitcherDialogDirections
                    .actionGlobalAccountSwitcherDialog(viewModel.currentUser, viewModel.users.toTypedArray())
                navController.navigate(action)
                true
            }
        }
    }
}

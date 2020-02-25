package com.kainalu.readerforreddit

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.kainalu.readerforreddit.databinding.ActivityMainBinding
import com.kainalu.readerforreddit.di.Injector
import com.kainalu.readerforreddit.ui.AccountSwitcherDialogDirections
import com.kainalu.readerforreddit.util.viewBinding

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Injector.get().inject(this)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigation.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.submissionFragment || destination.id == R.id.authFragment) {
                binding.bottomNavigation.visibility = View.GONE
            } else {
                binding.bottomNavigation.visibility = View.VISIBLE
            }
        }

        // We have to use a layout change listener to override the long click of the profile view
        binding.bottomNavigation.addOnLayoutChangeListener { v, _, _, _, _, _, _, _, _ ->
            val view = v.rootView.findViewById<View>(R.id.profile)
            view.setOnLongClickListener {
                val action = AccountSwitcherDialogDirections.actionGlobalAccountSwitcherDialog()
                navController.navigate(action)
                true
            }
        }
    }
}

package com.kainalu.readerforreddit.subscription

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.kainalu.readerforreddit.R
import com.kainalu.readerforreddit.databinding.FragmentSubscriptionsBinding
import com.kainalu.readerforreddit.di.Injector
import com.kainalu.readerforreddit.di.ViewModelFactory
import com.kainalu.readerforreddit.network.models.Subreddit
import com.kainalu.readerforreddit.util.viewBinding
import javax.inject.Inject

class SubscriptionsFragment : Fragment(R.layout.fragment_subscriptions),
    SubscriptionController.SubredditClickListener {

    private val binding by viewBinding(FragmentSubscriptionsBinding::bind)
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewmodel by viewModels<SubscriptionsViewModel> { viewModelFactory }
    private val controller = SubscriptionController(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Injector.get().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.setController(controller)
        viewmodel.subscriptions.observe(viewLifecycleOwner, Observer {
            controller.setData(it)
        })
    }

    override fun onSubredditClicked(subreddit: Subreddit) {
        val action =
            SubscriptionsFragmentDirections.actionSubscriptionsFragmentToSubredditFragment(subreddit.displayName)
        findNavController().navigate(action)
    }
}
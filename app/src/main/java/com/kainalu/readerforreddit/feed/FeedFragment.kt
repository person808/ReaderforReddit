package com.kainalu.readerforreddit.feed


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.kainalu.readerforreddit.R
import com.kainalu.readerforreddit.di.Injector
import com.kainalu.readerforreddit.models.LinkData
import kotlinx.android.synthetic.main.fragment_feed.*
import javax.inject.Inject

class FeedFragment : Fragment(), LinkController.LinkClickListener {

    private val headerClickListener = View.OnClickListener { v ->
        PopupMenu(requireContext(), v).run {
            menuInflater.inflate(R.menu.subreddit_sort, menu)
            if (viewModel.viewState.value?.availableSorts?.contains(SubredditSort.BEST) == false) {
                menu.removeItem(R.id.best)
            }
            listOf(R.id.controversial, R.id.top).forEach {
                val menuItem = menu.findItem(it)
                menuInflater.inflate(R.menu.sort_durations, menuItem.subMenu)
            }
            var selectedSort: SubredditSort? = null
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.best -> {
                        viewModel.setSort(SubredditSort.BEST)
                        true
                    }
                    R.id.hot -> {
                        viewModel.setSort(SubredditSort.HOT)
                        true
                    }
                    R.id.new_sort -> {
                        viewModel.setSort(SubredditSort.NEW)
                        true
                    }
                    R.id.top -> {
                        selectedSort = SubredditSort.TOP
                        true
                    }
                    R.id.controversial -> {
                        selectedSort = SubredditSort.CONTROVERSIAL
                        true
                    }
                    R.id.rising -> {
                        viewModel.setSort(SubredditSort.RISING)
                        true
                    }
                    R.id.hour -> {
                        viewModel.setSort(selectedSort!!, Duration.HOUR)
                        true
                    }
                    R.id.day -> {
                        viewModel.setSort(selectedSort!!, Duration.DAY)
                        true
                    }
                    R.id.week -> {
                        viewModel.setSort(selectedSort!!, Duration.WEEK)
                        true
                    }
                    R.id.month -> {
                        viewModel.setSort(selectedSort!!, Duration.MONTH)
                        true
                    }
                    R.id.year -> {
                        viewModel.setSort(selectedSort!!, Duration.YEAR)
                        true
                    }
                    R.id.all_time -> {
                        viewModel.setSort(selectedSort!!, Duration.ALL)
                        true
                    }
                    else -> false
                }
            }
            show()
        }
    }
    private val controller = LinkController(headerClickListener, this)

    @Inject
    lateinit var viewModel: FeedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Injector.get().inject(this)
        viewModel.init()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.apply {
            setController(controller)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        swipeRefreshLayout.setOnRefreshListener { viewModel.refresh() }
        viewModel.feed.observe(viewLifecycleOwner, Observer { controller.submitList(it) })
        viewModel.viewState.observe(viewLifecycleOwner, Observer { render(it) })
    }

    private fun render(viewState: FeedViewState) {
        val headerLabel = if (viewState.sortDuration == Duration.NONE) {
            getString(viewState.sort.label)
        } else {
            "${getString(viewState.sort.label)} ${getString(viewState.sortDuration.label)}"
        }
        controller.headerLabel = headerLabel

        swipeRefreshLayout.isRefreshing = viewState.loading
    }

    override fun onLinkClicked(link: LinkData) {
        val action = FeedFragmentDirections.actionFeedFragmentToSubmissionFragment(link.subreddit, link.id)
        findNavController().navigate(action)
    }
}

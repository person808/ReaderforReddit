package com.kainalu.readerforreddit.submission

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.kainalu.readerforreddit.R
import com.kainalu.readerforreddit.databinding.FragmentSubmissionBinding
import com.kainalu.readerforreddit.di.Injector
import com.kainalu.readerforreddit.di.ViewModelFactory
import com.kainalu.readerforreddit.models.LinkData
import com.kainalu.readerforreddit.tree.CommentNode
import com.kainalu.readerforreddit.tree.MoreNode
import com.kainalu.readerforreddit.tree.VisibilityState
import com.kainalu.readerforreddit.util.viewBinding
import javax.inject.Inject

class SubmissionFragment : Fragment(R.layout.fragment_submission),
    SubmissionController.CommentClickListener,
    SubmissionController.LinkClickListener, SubmissionController.MoreClickListener {

    private val args by navArgs<SubmissionFragmentArgs>()

    private val binding by viewBinding(FragmentSubmissionBinding::bind)
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by viewModels<SubmissionViewModel> { viewModelFactory }

    private val headerClickListener = View.OnClickListener { v ->
        PopupMenu(requireContext(), v).run {
            menuInflater.inflate(R.menu.comment_sort, menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.best -> {
                        viewModel.setSort(SubmissionSort.BEST)
                        true
                    }
                    R.id.top -> {
                        viewModel.setSort(SubmissionSort.TOP)
                        true
                    }
                    R.id.new_sort -> {
                        viewModel.setSort(SubmissionSort.NEW)
                        true
                    }
                    R.id.controversial -> {
                        viewModel.setSort(SubmissionSort.CONTROVERSIAL)
                        true
                    }
                    R.id.old -> {
                        viewModel.setSort(SubmissionSort.OLD)
                        true
                    }
                    R.id.qa -> {
                        viewModel.setSort(SubmissionSort.QA)
                        true
                    }
                    else -> false
                }
            }
            show()
        }
    }
    private val controller = SubmissionController(this, headerClickListener, this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Injector.get().inject(this)
        viewModel.init(args.subreddit, args.threadId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.apply {
            setController(controller)
            addItemDecoration(CommentItemDecoration(context))
        }
        binding.swipeRefreshLayout.setOnRefreshListener { viewModel.refresh() }
        viewModel.viewState.observe(viewLifecycleOwner, Observer { render(it) })
    }

    override fun onCommentClicked(commentNode: CommentNode) {
        if (commentNode.visibility != VisibilityState.VISIBLE) {
            viewModel.expandComment(commentNode)
        } else {
            viewModel.collapseComment(commentNode)
        }
    }

    override fun onLinkClicked(link: LinkData) {
        val intent = CustomTabsIntent.Builder().build()
        intent.launchUrl(requireContext(), Uri.parse(link.url))
    }

    override fun onMoreClicked(more: MoreNode) {
        viewModel.getChildren(more)
    }

    private fun render(viewState: SubmissionViewState) {
        controller.setData(viewState.submissionTree, viewState.comments, viewState.sort)
        binding.swipeRefreshLayout.isRefreshing = viewState.loading
    }
}
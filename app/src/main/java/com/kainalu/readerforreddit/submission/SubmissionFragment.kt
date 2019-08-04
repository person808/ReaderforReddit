package com.kainalu.readerforreddit.submission

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.kainalu.readerforreddit.R
import com.kainalu.readerforreddit.di.Injector
import com.kainalu.readerforreddit.di.ViewModelFactory
import com.kainalu.readerforreddit.models.LinkData
import com.kainalu.readerforreddit.tree.CommentNode
import com.kainalu.readerforreddit.tree.MoreNode
import com.kainalu.readerforreddit.tree.VisibilityState
import kotlinx.android.synthetic.main.fragment_submission.*
import javax.inject.Inject

class SubmissionFragment : Fragment(), SubmissionController.CommentClickListener,
    SubmissionController.LinkClickListener, SubmissionController.MoreClickListener {

    private val args by navArgs<SubmissionFragmentArgs>()

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_submission, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.apply {
            setController(controller)
            addItemDecoration(CommentItemDecoration(context))
        }
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
        controller.setData(viewState.link, viewState.comments, viewState.sort)
    }
}
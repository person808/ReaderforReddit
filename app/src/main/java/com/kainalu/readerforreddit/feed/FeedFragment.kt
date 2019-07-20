package com.kainalu.readerforreddit.feed


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.airbnb.epoxy.EpoxyRecyclerView
import com.kainalu.readerforreddit.R
import com.kainalu.readerforreddit.di.Injector
import javax.inject.Inject

class FeedFragment : Fragment() {

    private lateinit var recyclerView: EpoxyRecyclerView
    private val controller = LinkController()
    @Inject
    lateinit var viewModel: FeedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Injector.get().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById<EpoxyRecyclerView>(R.id.recyclerView).apply {
            setControllerAndBuildModels(controller)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        viewModel.getFeed().observe(viewLifecycleOwner, Observer { controller.submitList(it) })
    }
}

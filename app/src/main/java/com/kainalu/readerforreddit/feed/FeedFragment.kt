package com.kainalu.readerforreddit.feed


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kainalu.readerforreddit.R
import com.kainalu.readerforreddit.di.Injector
import javax.inject.Inject

class FeedFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val adapter = LinkPagedAdapter()
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
        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView).apply {
            adapter = this@FeedFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(MarginItemDecoration(resources.getDimension(R.dimen.recyclerview_item_margin).toInt()))
        }
        viewModel.getFeed().observe(viewLifecycleOwner, Observer { adapter.submitList(it) })
    }
}

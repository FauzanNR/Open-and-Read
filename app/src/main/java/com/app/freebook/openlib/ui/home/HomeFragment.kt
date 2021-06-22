package com.app.freebook.openlib.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.app.freebook.core.ui.FragmentModel
import com.app.freebook.openlib.R
import com.app.freebook.openlib.databinding.HomeFragmentBinding
import com.bumptech.glide.Glide
import org.koin.android.viewmodel.ext.android.viewModel


class HomeFragment : FragmentModel() {

    private var binding: HomeFragmentBinding? = null
    private val viewModel: HomeViewModel by viewModel()
    private val bookHomeAdapter = BookHomeAdapter()

    override fun onDisconnected() {
        if (binding?.root?.isVisible == true) {
            binding?.idTextInfoBook?.visibility = View.VISIBLE
            binding?.idProgressBook?.visibility =
                View.GONE
        }
    }

    override fun onConnected() {
        setData()
    }

    override fun onResume() {
        super.onResume()
        if (isConnected && binding?.root?.isVisible == true) {
            setData()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            HomeFragmentBinding.bind(inflater.inflate(R.layout.home_fragment, container, false))
        val gridCount = resources.getInteger(R.integer.grid_column_count)
        bookHomeAdapter.notifyDataSetChanged()
        binding?.idRecyclerBook?.apply {
            layoutManager = GridLayoutManager(requireContext(), gridCount)
            adapter = bookHomeAdapter
        }

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Glide.get(this.requireContext().applicationContext)
            .clearMemory()
        binding?.idRecyclerBook?.adapter = null
        binding = null
    }

    private fun setData() {
        if (binding?.root?.isVisible == true && this@HomeFragment.isAdded) {
            Log.d("IsAdded", "HomeFragment")
            binding?.idTextInfoBook?.visibility = View.INVISIBLE
            binding?.idProgressBook?.visibility =
                View.VISIBLE
            viewModel.getAllBook().observe(viewLifecycleOwner, {
                when (it) {
                    is com.app.freebook.core.data.Resource.Loading -> binding?.idProgressBook?.visibility =
                        View.VISIBLE
                    is com.app.freebook.core.data.Resource.Error -> {
                        binding?.idTextInfoBook?.visibility = View.VISIBLE
                        binding?.idProgressBook?.visibility = View.GONE
                    }
                    is com.app.freebook.core.data.Resource.Success -> {
                        binding?.idProgressBook?.visibility = View.GONE
                        it.data?.let { it1 -> bookHomeAdapter.setDataAdapter(it1) }
                    }
                    else -> {
                        binding?.idTextInfoBook?.visibility = View.VISIBLE
                        binding?.idProgressBook?.visibility = View.GONE
                    }
                }
            })
        }
    }
}
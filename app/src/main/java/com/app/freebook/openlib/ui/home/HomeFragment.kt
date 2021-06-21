package com.app.freebook.openlib.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.app.freebook.core.ui.FragmentModel
import com.app.freebook.openlib.R
import com.app.freebook.openlib.databinding.HomeFragmentBinding
import org.koin.android.viewmodel.ext.android.viewModel


class HomeFragment : FragmentModel() {

    private var binding: HomeFragmentBinding? = null
    private val viewModel: HomeViewModel by viewModel()
    private val bookHomeAdapter = BookHomeAdapter()

    companion object {
        var isConnected = false
    }

    override fun onDisconnected() {
//        if (this.isAdded && this.isVisible) {
        binding?.idTextInfoBook?.visibility = View.VISIBLE
        binding?.idProgressBook?.visibility = View.GONE
//        }
        isConnected = false
    }

    override fun onConnected() {
        isConnected = true
        setData()
    }

    override fun onResume() {
        super.onResume()
        if (isConnected) setData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
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
        binding?.idRecyclerBook?.adapter = null
        binding = null
    }
    private fun setData() {
        if (this.isAdded) {
            Log.d("IsAdded", "HomeFragment")
            binding?.idTextInfoBook?.visibility = View.INVISIBLE
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
                }
            })
        }
    }
}
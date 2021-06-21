package com.app.freebook.favorite.favorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.app.freebook.favorite.R
import com.app.freebook.favorite.databinding.FavoriteFragmentBinding
import com.app.freebook.favorite.di.favoriteModule
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class FavoriteFragment : Fragment() {
    private var binding: FavoriteFragmentBinding? = null
    private val viewModel: FavoriteViewModel by viewModel()
    private val favoriteAdapter = FavoriteAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loadKoinModules(favoriteModule)
        binding = FavoriteFragmentBinding.bind(
            inflater.inflate(
                R.layout.favorite_fragment,
                container,
                false
            )
        )
        val gridCount = resources.getInteger(com.app.freebook.core.R.integer.grid_column_count)
        favoriteAdapter.notifyDataSetChanged()
        binding?.idRecyclerBookFav?.apply {
            layoutManager = GridLayoutManager(requireContext(), gridCount)
            adapter = favoriteAdapter
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.idRecyclerBookFav?.adapter = null
        binding = null
    }

    private fun observeData() {
        if (this.isAdded) {
            binding?.idProgressBookFav?.visibility = View.VISIBLE
            viewModel.getAllBook().observe(viewLifecycleOwner, {
                when (it) {
                    is com.app.freebook.core.data.Resource.Success -> {
                        binding?.idTextInfoBookFav?.visibility = View.INVISIBLE
                        binding?.idProgressBookFav?.visibility = View.INVISIBLE
                        binding?.idRecyclerBookFav?.visibility = View.VISIBLE
                        it.data?.let { it1 -> favoriteAdapter.setDataAdapter(it1) }
                    }
                    is com.app.freebook.core.data.Resource.Error -> Log.d(
                        "observeData",
                        it.message.toString()
                    )
                    else -> {
                        binding?.idRecyclerBookFav?.visibility = View.INVISIBLE
                        binding?.idProgressBookFav?.visibility = View.INVISIBLE
                        binding?.idTextInfoBookFav?.visibility = View.VISIBLE
                        Log.d("observeData", "Else: " + it.data.toString())
                    }
                }
            })
        }
    }
}
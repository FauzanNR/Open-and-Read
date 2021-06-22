package com.app.freebook.openlib.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.freebook.core.domain.model.Book
import com.app.freebook.core.ui.FragmentModel
import com.app.freebook.openlib.R
import com.app.freebook.openlib.databinding.DetailFragmentBinding
import com.app.freebook.openlib.ui.home.BookHomeAdapter.Companion.EXTRA_DETAIL
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.android.viewmodel.ext.android.viewModel

class DetailFragment : FragmentModel(), View.OnClickListener {
    private lateinit var bottomNavigationView: BottomNavigationView
    private var binding: DetailFragmentBinding? = null
    private val viewModel: DetailViewModel by viewModel()
    private var detail: Book? = null

    companion object {
        private lateinit var identifier: String
        private var isEmpty = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DetailFragmentBinding.bind(inflater.inflate(R.layout.detail_fragment, container, false))
        binding?.apply {
            idFavoriteBtn.setOnClickListener(this@DetailFragment)
            idShareBtn.setOnClickListener(this@DetailFragment)
            idReadBtn.setOnClickListener(this@DetailFragment)
            expandableFab.setOnClickListener(this@DetailFragment)
        }
        return binding?.root
    }

    override fun onDisconnected() {
        if (this.isVisible && this.isAdded) setData(false)
    }

    override fun onConnected() {
        if (this.isVisible && this.isAdded) setData(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavigationView =
            requireActivity().findViewById(R.id.bottomNavigationView) as BottomNavigationView
        bottomNavigationView.visibility = View.GONE
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        Glide.get(this.requireContext())
            .clearMemory()
        bottomNavigationView.visibility = View.VISIBLE
        (activity as AppCompatActivity).supportActionBar?.show()
    }

    private fun setData(s: Boolean) {
        try {
            if (this.isAdded and this.isVisible) {
                if (arguments != null) {
                    arguments?.let {
                        val data = it.getString(EXTRA_DETAIL)
                        when {
                            it.containsKey(EXTRA_DETAIL) -> {
                                binding?.apply {
                                    if (data != null) {
                                        viewModel.getDetail(data, s)
                                            .observe(viewLifecycleOwner, { itBook ->
                                                Log.d(
                                                    "viewLifecycleOwner",
                                                    itBook.data.toString()
                                                )
                                                when (itBook) {
                                                    is com.app.freebook.core.data.Resource.Loading -> Log.d(
                                                        "loading",
                                                        EXTRA_DETAIL
                                                    )
                                                    is com.app.freebook.core.data.Resource.Error -> Log.d(
                                                        "Error",
                                                        itBook.message.toString()
                                                    )
                                                    is com.app.freebook.core.data.Resource.Success -> {
                                                        detail = itBook.data as Book
                                                        identifier = detail?.identifier.toString()
                                                        idDetailCollap.title = detail?.title
                                                        idWriterData.text = detail?.creator
                                                        idPublisherData.text = detail?.publisher
                                                        idPublicationData.text = detail?.year
                                                        idLangData.text = detail?.language
                                                        idDetailDescriptionData.text =
                                                            detail?.description
                                                        context?.let { it1 ->
                                                            Glide.with(it1)
                                                                .load("https://archive.org/download/${detail?.identifier}/page/cover_w160.jpg")
                                                                .apply(RequestOptions())
                                                                .placeholder(R.drawable.ic_menu_book)
                                                                .error(R.drawable.ic_broken_image)
                                                                .into(idDetailImg)
                                                        }
                                                    }
                                                }
                                                checkData(identifier)
                                            })
                                    }
                                }
                            }
                            else -> {
                                Log.d(EXTRA_DETAIL, data.toString())
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.d("SET_DATA", e.toString())
        }
    }

    private fun addOrDelete() {
        if (!TextUtils.isEmpty(identifier)) {
            if (detail != null) {
                if (checkData(identifier)) {
                    viewModel.addBook(detail!!)
                    Toast.makeText(context, "Book added to favorite!", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    viewModel.deleteBook(identifier)
                    Toast.makeText(context, "Book deleted to favorite!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun checkData(identifier: String): Boolean {
        viewModel.search(identifier).observe(viewLifecycleOwner, {
            isEmpty = it.data.isNullOrEmpty()
            changeIcon(it.data.isNullOrEmpty())
            Log.d("checkData", isEmpty.toString())
        })
        return isEmpty
    }

    private fun changeIcon(b: Boolean) {
        if (b) {
            binding?.idFavoriteBtn?.setImageResource(R.drawable.ic_favorite_border_btn)
        } else {
            binding?.idFavoriteBtn?.setImageResource(R.drawable.ic_favorite_btn)
        }
    }

    private fun share() {
        val intent: Intent = Intent(Intent.ACTION_SEND).setType("text/plain")
            .putExtra(
                Intent.EXTRA_TEXT,
                "Read this interesting book http://www.archive.org/details/aliceinwonderlan00carriala"
            )
        startActivity(Intent.createChooser(intent, "Share Via"))
    }

    private fun readBook() {
        val intent: Intent =
            Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://www.archive.org/details/aliceinwonderlan00carriala"))
        startActivity(Intent.createChooser(intent, "Open with"))
    }

    override fun onClick(v: View?) {
        when (v) {
            binding?.idFavoriteBtn -> addOrDelete()

            binding?.idShareBtn -> share()

            binding?.idReadBtn -> readBook()

            binding?.expandableFab -> checkData(identifier)
        }
    }
}
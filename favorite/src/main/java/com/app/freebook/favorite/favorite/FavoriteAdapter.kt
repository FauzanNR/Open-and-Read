package com.app.freebook.favorite.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.app.freebook.core.domain.model.Book
import com.app.freebook.openlib.R
import com.app.freebook.openlib.databinding.ItemCardBinding
import com.app.freebook.openlib.ui.home.BookHomeAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.FavoViewHolder>() {

    private var dataBook = ArrayList<Book>()

    fun setDataAdapter(data: List<Book>) {
        dataBook.clear()
        dataBook.addAll(data)
        notifyDataSetChanged()
    }

    class FavoViewHolder(private val bindItem: ItemCardBinding) :
        RecyclerView.ViewHolder(bindItem.root) {
        fun bind(book: Book) {
            bindItem.apply {
                idItemTitle.text = book.title
                idItemDescription.text =
                    if (book.description == null) "No description!!!" else book.description
                idItemYear.text = book.year
                Glide.with(itemView.context)
                    .load("https://archive.org/download/${book.identifier}/page/cover_w160.jpg")
                    .apply(RequestOptions())
                    .into(idItemImg)

                itemView.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putString(BookHomeAdapter.EXTRA_DETAIL, book.identifier)
                    Navigation.findNavController(itemView).navigate(R.id.detailFragment, bundle)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoViewHolder, position: Int) {
        val data = dataBook[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = dataBook.size
}
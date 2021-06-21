package com.app.freebook.openlib.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.app.freebook.core.domain.model.Book
import com.app.freebook.openlib.R
import com.app.freebook.openlib.databinding.ItemCardBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class BookHomeAdapter : RecyclerView.Adapter<BookHomeAdapter.BookViewHolder>() {

    private var dataBook = ArrayList<Book>()

    companion object {
        val EXTRA_DETAIL = "EXTRA_DETAIL"
    }

    fun setDataAdapter(data: List<Book>) {
        dataBook.clear()
        dataBook.addAll(data)
        notifyDataSetChanged()
    }

    class BookViewHolder(private val item: ItemCardBinding) :
        RecyclerView.ViewHolder(item.root) {
        fun bind(book: Book) {
            item.apply {
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
                    bundle.putString(EXTRA_DETAIL, book.identifier)
                    Navigation.findNavController(itemView).navigate(R.id.detailFragment, bundle)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding =
            ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val data = dataBook[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = dataBook.size

}
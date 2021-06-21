package com.app.freebook.core.util

import com.app.freebook.core.data.local.room.BookEntity
import com.app.freebook.core.data.remote.network.response.detail.Metadata
import com.app.freebook.core.data.remote.network.response.listdata.Doc
import com.app.freebook.core.domain.model.Book

object DataMapper {

    fun responseToListBook(data: List<Doc>?): List<Book> {
        val listBook = ArrayList<Book>()
        if (!(data.isNullOrEmpty())) {
            data.map { itMap ->
                val book = itMap.let {
                    val year = it.year.toString()
                    val yearD = year.toDouble()
                    val yearI = yearD.toInt()
                    Book(
                        creator = it.creator.toString(),
                        description = (it.description ?: "No Description!").toString(),
                        identifier = it.identifier.toString(),
                        language = it.language.toString(),
                        publisher = it.publisher.toString(),
                        title = it.title.toString(),
                        year = yearI.toString()
                    )
                }
                listBook.add(book)
            }
        }
        return listBook
    }

    fun responseDetailToBook(data: Metadata?): Book {
        return Book(
            creator = data?.creator.toString(),
            description = (data?.description ?: "No Description!").toString(),
            identifier = data?.identifier?.first().toString(),
            language = data?.language.toString(),
            publisher = data?.publisher.toString(),
            title = data?.title?.first().toString(),
            year = data?.date?.first().toString()
        )
    }

    fun entityToBook(data: List<BookEntity>?): List<Book> {
        val listBook = ArrayList<Book>()
        if (!(data.isNullOrEmpty())) {
            data.map { itMap ->
                val book = itMap.let {
                    Book(
                        creator = it.creator.toString(),
                        description = (it.description ?: "No Description!").toString(),
                        identifier = it.identifier.toString(),
                        language = it.language.toString(),
                        publisher = it.publisher.toString(),
                        title = it.title.toString(),
                        year = it.year.toString()
                    )
                }
                listBook.add(book)
            }
        }
        return listBook
    }

    fun bookToEntity(data: Book?): BookEntity {
        return BookEntity(
            creator = data?.creator.toString(),
            description = (data?.description ?: "No Description!").toString(),
            identifier = data?.identifier.toString(),
            language = data?.language.toString(),
            publisher = data?.publisher.toString(),
            title = data?.title,
            year = data?.year.toString()
        )
    }
}
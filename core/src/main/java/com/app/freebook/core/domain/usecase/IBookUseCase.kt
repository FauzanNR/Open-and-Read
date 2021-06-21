package com.app.freebook.core.domain.usecase

import com.app.freebook.core.domain.model.Book
import kotlinx.coroutines.flow.Flow

interface IBookUseCase {
    fun getAllBooks(s: Boolean): Flow<com.app.freebook.core.data.Resource<List<Book>>>
    fun getDetailBook(
        identifier: String,
        s: Boolean
    ): Flow<com.app.freebook.core.data.Resource<Book>>

    suspend fun addBook(book: Book)
    suspend fun deleteBook(key: String)
    fun searchBook(identifier: String): Flow<com.app.freebook.core.data.Resource<List<Book>>>
}
package com.app.freebook.core.domain.usecase

import com.app.freebook.core.domain.model.Book
import com.app.freebook.core.domain.repository.IRepository
import kotlinx.coroutines.flow.Flow

class BookInteractor(private val repository: IRepository) : IBookUseCase {

    override fun getAllBooks(s: Boolean): Flow<com.app.freebook.core.data.Resource<List<Book>>> =
        repository.getAllBooks(s)

    override fun getDetailBook(
        identifier: String,
        s: Boolean
    ): Flow<com.app.freebook.core.data.Resource<Book>> =
        repository.getDetailBook(identifier, s)

    override suspend fun addBook(book: Book) = repository.addBook(book)
    override suspend fun deleteBook(key: String) = repository.deleteBook(key)

    override fun searchBook(identifier: String): Flow<com.app.freebook.core.data.Resource<List<Book>>> =
        repository.searchBook(identifier)
}
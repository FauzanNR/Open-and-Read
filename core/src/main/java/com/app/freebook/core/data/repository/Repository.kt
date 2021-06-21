package com.app.freebook.core.data.repository

import com.app.freebook.core.data.remote.RemoteDataSource
import com.app.freebook.core.domain.model.Book
import com.app.freebook.core.domain.repository.IRepository
import kotlinx.coroutines.flow.Flow

class Repository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: com.app.freebook.core.data.local.LocalDataSource
) : IRepository {
    override fun getAllBooks(s: Boolean): Flow<com.app.freebook.core.data.Resource<List<Book>>> {

        return if (s) {
            remoteDataSource.getAllBooks()
        } else {
            localDataSource.getAllFavBooks()
        }
    }

    override fun getDetailBook(
        identifier: String,
        s: Boolean
    ): Flow<com.app.freebook.core.data.Resource<Book>> =
        if (s) remoteDataSource.getDetailBook(identifier) else localDataSource.getBook(identifier)

    override suspend fun addBook(book: Book) = localDataSource.addBook(book)
    override suspend fun deleteBook(key: String) = localDataSource.deleteBook(key)

    override fun searchBook(identifier: String): Flow<com.app.freebook.core.data.Resource<List<Book>>> =
        localDataSource.searchBook(identifier)

}
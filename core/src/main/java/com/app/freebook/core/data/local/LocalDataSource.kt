package com.app.freebook.core.data.local

import android.util.Log
import com.app.freebook.core.data.local.room.BookDao
import com.app.freebook.core.domain.model.Book
import com.app.freebook.core.util.DataMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class LocalDataSource(private val dao: BookDao) {

    fun getAllFavBooks(): Flow<com.app.freebook.core.data.Resource<List<Book>>> =
        flow {
            try {
                val data = dao.getAllFavBooks()
                if (!(data.first().isNullOrEmpty())) {
                    data.collect {
                        emit(
                            com.app.freebook.core.data.Resource.Success(
                                DataMapper.entityToBook(
                                    it
                                )
                            )
                        )
                    }
                } else if (data.first().isNullOrEmpty()) {
                    data.collect {
                        emit(
                            com.app.freebook.core.data.Resource.Loading(
                                DataMapper.entityToBook(
                                    it
                                )
                            )
                        )
                    }
                } else {
                    data.collect {
                        emit(
                            com.app.freebook.core.data.Resource.Error(
                                "Local Error",
                                DataMapper.entityToBook(it)
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                emit(
                    com.app.freebook.core.data.Resource.Error(
                        e.message.toString(),
                        DataMapper.entityToBook(null)
                    )
                )
            }
        }.flowOn(Dispatchers.IO)


    fun searchBook(identifier: String): Flow<com.app.freebook.core.data.Resource<List<Book>>> =
        flow {
            try {
                val data = dao.searchBook(identifier)
                if (!(data.isNullOrEmpty())) {
                    emit(com.app.freebook.core.data.Resource.Success(DataMapper.entityToBook(data)))
                } else if (data.isNullOrEmpty()) {
                    emit(com.app.freebook.core.data.Resource.Loading(DataMapper.entityToBook(data)))
                } else {
                    data.map {
                        emit(
                            com.app.freebook.core.data.Resource.Error(
                                "getAllBooks",
                                DataMapper.entityToBook(data)
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                emit(
                    com.app.freebook.core.data.Resource.Error(
                        e.message.toString(),
                        DataMapper.entityToBook(null)
                    )
                )
            }
        }.flowOn(Dispatchers.IO)

    fun getBook(identifier: String): Flow<com.app.freebook.core.data.Resource<Book>> = flow {
        try {
            val data = dao.getBook(identifier)
            Log.d("getBook", data.toString())
            if (!(data.first().equals(null))) {
                data.collect {
                    Log.d("getBook_Collect", it.toString())
                    emit(
                        com.app.freebook.core.data.Resource.Success(
                            DataMapper.entityToBook(
                                listOf(
                                    it
                                )
                            )[0]
                        )
                    )
                }
            } else if (data.first().equals(null)) {
                data.collect {
                    emit(
                        com.app.freebook.core.data.Resource.Loading(
                            DataMapper.entityToBook(
                                listOf(
                                    it
                                )
                            )[0]
                        )
                    )
                }
            }
        } catch (e: Exception) {
            Log.d("Error", e.message.toString())
        }
    }.flowOn(Dispatchers.IO)

    suspend fun addBook(book: Book) {
        try {
            dao.addBook(DataMapper.bookToEntity(book))
        } catch (e: Exception) {
            Log.d(
                "AddBook Error: ",
                e.message.toString() + DataMapper.bookToEntity(book).toString()
            )
        }
    }

    suspend fun deleteBook(key: String) {
        try {
            dao.delete(key)
        } catch (e: Exception) {
            Log.d("Delete Error: ", e.message.toString())
        }
    }
}
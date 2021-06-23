package com.app.freebook.core.data.remote

import android.util.Log
import com.app.freebook.core.data.remote.network.ApiService
import com.app.freebook.core.domain.model.Book
import com.app.freebook.core.util.DataMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okio.IOException
import java.net.ConnectException

class RemoteDataSource(private val service: ApiService) {
    fun getAllBooks(): Flow<com.app.freebook.core.data.Resource<List<Book>>> = flow {
        try {
            val responseData = service.getListBook()
            val dataList = responseData.body()?.response?.docs
            if (responseData.isSuccessful && dataList != null) {
                emit(
                    com.app.freebook.core.data.Resource.Success(
                        DataMapper.responseToListBook(
                            dataList
                        )
                    )
                )
            } else if (!(responseData.isSuccessful) or (dataList?.isEmpty() == true)) {
                emit(com.app.freebook.core.data.Resource.Loading(dataList?.let {
                    DataMapper.responseToListBook(
                        it
                    )
                }))
            } else {
                emit(
                    com.app.freebook.core.data.Resource.Error(responseData.errorBody().toString(),
                        dataList?.let { DataMapper.responseToListBook(it) })
                )
            }
        } catch (e: Exception) {
            Log.d("RemoteResponse", e.toString())
        } catch (e: ConnectException) {
            Log.d("RemoteResponse", e.toString())
        } catch (e: IOException) {
            Log.d("RemoteResponse", e.toString())
        }
    }.flowOn(Dispatchers.IO)

    fun getDetailBook(identifier: String): Flow<com.app.freebook.core.data.Resource<Book>> {
        return flow {
            try {
                val responseDetail = service.getDetailBook(identifier)
                val data = responseDetail.body()?.metadata
                if (responseDetail.isSuccessful && data != null) {
                    emit(
                        com.app.freebook.core.data.Resource.Success(
                            DataMapper.responseDetailToBook(
                                data
                            )
                        )
                    )
                } else if (!(responseDetail.isSuccessful) or (data == null) == true) {
                    emit(com.app.freebook.core.data.Resource.Loading(data?.let {
                        DataMapper.responseDetailToBook(
                            it
                        )
                    }))
                } else {
                    emit(
                        com.app.freebook.core.data.Resource.Error(
                            responseDetail.errorBody().toString(),
                            data?.let { DataMapper.responseDetailToBook(it) }
                        )
                    )
                }
            } catch (e: Exception) {
                Log.d("RemoteResponse", e.toString())
            } catch (e: ConnectException) {
                Log.d("RemoteResponse", e.toString())
            } catch (e: IOException) {
                Log.d("RemoteResponse", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

}
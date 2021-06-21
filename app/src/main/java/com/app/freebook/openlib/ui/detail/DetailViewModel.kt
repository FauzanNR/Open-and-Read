package com.app.freebook.openlib.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.app.freebook.core.domain.model.Book
import com.app.freebook.core.domain.usecase.IBookUseCase
import kotlinx.coroutines.launch

class DetailViewModel(private val useCase: IBookUseCase) : ViewModel() {
    fun getDetail(
        identifier: String,
        s: Boolean
    ): LiveData<com.app.freebook.core.data.Resource<Book>> =
        useCase.getDetailBook(identifier, s).asLiveData()

    fun search(identifier: String) = useCase.searchBook(identifier).asLiveData()

    fun addBook(book: Book) = viewModelScope.launch {
        useCase.addBook(book)
    }

    fun deleteBook(key: String) = viewModelScope.launch {
        useCase.deleteBook(key)
    }
}
package com.app.freebook.favorite.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.app.freebook.core.domain.usecase.IBookUseCase

class FavoriteViewModel(private val bookUseCase: IBookUseCase) : ViewModel() {
    fun getAllBook() = bookUseCase.getAllBooks(false).asLiveData()
}
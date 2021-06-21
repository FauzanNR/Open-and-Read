package com.app.freebook.openlib.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.app.freebook.core.domain.usecase.IBookUseCase

class HomeViewModel(private val bookUseCase: IBookUseCase) : ViewModel() {
    fun getAllBook() = bookUseCase.getAllBooks(true).asLiveData()
}
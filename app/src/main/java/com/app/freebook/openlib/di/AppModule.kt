package com.app.freebook.openlib

import com.app.freebook.core.domain.usecase.BookInteractor
import com.app.freebook.core.domain.usecase.IBookUseCase
import com.app.freebook.openlib.ui.detail.DetailViewModel
import com.app.freebook.openlib.ui.home.HomeViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val useCaseModule = module {
    factory<IBookUseCase> { BookInteractor(get()) }
}

val viewModeModule: Module = module {
    viewModel { HomeViewModel(get()) }
    viewModel { DetailViewModel(get()) }
}
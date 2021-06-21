package com.app.freebook.core.di

import androidx.room.Room
import com.app.freebook.core.data.local.room.FavoriteBooksDB
import com.app.freebook.core.data.remote.RemoteDataSource
import com.app.freebook.core.data.remote.network.ApiService
import com.app.freebook.core.data.repository.Repository
import com.app.freebook.core.domain.repository.IRepository
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val baseUrl = "https://archive.org/"
val networkModule = module {
    single {
        OkHttpClient.Builder().apply {
            addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
            connectTimeout(20, TimeUnit.SECONDS)
            readTimeout(20, TimeUnit.SECONDS)
            writeTimeout(20, TimeUnit.SECONDS)
        }.build()
    }
    single {
        val retrofit = Retrofit.Builder().apply {
            baseUrl(baseUrl)
            addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            client(get())
        }.build()
        retrofit.create(ApiService::class.java)
    }
}


val roomModule = module {
    factory { get<FavoriteBooksDB>().getDao() }
    single {
        Room.databaseBuilder(
            androidApplication(),
            FavoriteBooksDB::class.java,
            "favorite_db",
        ).fallbackToDestructiveMigration()
            .build()
    }
}

val repositoryModule = module {
    single { RemoteDataSource(get()) }
    single { com.app.freebook.core.data.local.LocalDataSource(get()) }
    single<IRepository> { Repository(get(), get()) }
}

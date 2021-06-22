package com.app.freebook.core.di

import androidx.room.Room
import com.app.freebook.core.data.local.room.FavoriteBooksDB
import com.app.freebook.core.data.remote.RemoteDataSource
import com.app.freebook.core.data.remote.network.ApiService
import com.app.freebook.core.data.repository.Repository
import com.app.freebook.core.domain.repository.IRepository
import com.google.gson.GsonBuilder
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import okhttp3.CertificatePinner
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
        val hostName = "archive.org"
        val certificatePinner = CertificatePinner.Builder()
            .add(hostName, "sha256/cr5zEW+kToY4s0gRfx81qV0hJAVY1exO58jGFDaqvoQ=")
            .add(hostName, "sha256/8Rw90Ej3Ttt8RRkrg+WYDS9n7IS03bk5bjP/UXPtaY8=")
            .add(hostName, "sha256/Ko8tivDrEjiY90yGasP6ZpBU4jwXvHqVvQI0GS3GNdA=")
            .build()
        OkHttpClient.Builder().apply {
            addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
            connectTimeout(20, TimeUnit.SECONDS)
            readTimeout(20, TimeUnit.SECONDS)
            writeTimeout(20, TimeUnit.SECONDS)
            certificatePinner(certificatePinner)
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
    val passwordpharser: ByteArray = SQLiteDatabase.getBytes("freebookfauzan".toCharArray())
    val factory = SupportFactory(passwordpharser)
    single {
        Room.databaseBuilder(
            androidApplication(),
            FavoriteBooksDB::class.java,
            "favorite_db.db",
        ).fallbackToDestructiveMigration()
            .openHelperFactory(factory)
            .build()
    }
}

val repositoryModule = module {
    single { RemoteDataSource(get()) }
    single { com.app.freebook.core.data.local.LocalDataSource(get()) }
    single<IRepository> { Repository(get(), get()) }
}

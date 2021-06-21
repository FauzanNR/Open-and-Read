package com.app.freebook.core.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM favorite_book")
    fun getAllFavBooks(): Flow<List<BookEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBook(data: BookEntity)

    @Query("SELECT * FROM favorite_book WHERE identifier LIKE :key")
    fun getBook(key: String): Flow<BookEntity>

    @Query("SELECT * FROM favorite_book WHERE identifier LIKE :key ORDER BY identifier ASC")
    fun searchBook(key: String): List<BookEntity>

    @Query("DELETE FROM favorite_book WHERE identifier LIKE :key ")
    suspend fun delete(key: String)
}
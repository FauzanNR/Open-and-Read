package com.app.freebook.core.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BookEntity::class], version = 1)
abstract class FavoriteBooksDB : RoomDatabase() {
    abstract fun getDao(): BookDao
}
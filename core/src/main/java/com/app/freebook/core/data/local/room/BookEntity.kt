package com.app.freebook.core.data.local.room

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "favorite_book")
data class BookEntity(
    val creator: String?,
    val description: String?,
    @PrimaryKey
    @NonNull
    val identifier: String,
    val language: String?,
    val publisher: String?,
    val title: String?,
    val year: String?
) : Parcelable
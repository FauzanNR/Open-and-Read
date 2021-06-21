package com.app.freebook.core.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Book(
    val creator: String,
    val description: String?,
    val identifier: String,
    val language: String,
    val publisher: String,
    val title: String,
    val year: String
) : Parcelable
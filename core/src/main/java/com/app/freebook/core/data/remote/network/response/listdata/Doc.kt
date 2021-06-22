package com.app.freebook.core.data.remote.network.response.listdata

import androidx.annotation.Keep

@Keep
data class Doc(
    val creator: Any?,
    val description: Any?,
    val identifier: String?,
    val language: Any?,
    val publisher: Any?,
    val title: Any?,
    val year: Any?
)
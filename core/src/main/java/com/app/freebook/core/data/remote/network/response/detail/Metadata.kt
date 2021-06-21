package com.app.freebook.core.data.remote.network.response.detail

data class Metadata(
    val description: List<String>?,
    val creator: List<String>?,
    val date: List<String>?,
    val identifier: List<String>?,
    val isbn: List<String>?,
    val language: List<String>?,
    val publisher: List<String>?,
    val title: List<String>?,
)
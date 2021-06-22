package com.app.freebook.core.data.remote.network

import com.app.freebook.core.data.remote.network.response.detail.detail
import com.app.freebook.core.data.remote.network.response.listdata.ResponseData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("advancedsearch.php?q=openlibrary_work:(*)+AND+(collection:(inlibrary)+OR+(!collection:(printdisabled)))+AND+(lending___available_to_browse:true+OR+lending___available_to_borrow:true)+AND+openlibrary_subject:openlibrary_staff_picks&offset=2&output=json")
    suspend fun getListBook(): Response<ResponseData>

    @GET("https://archive.org/details/{identifier}&output=json")
    suspend fun getDetailBook(
        @Path("identifier") identifier: String
    ): Response<detail>
}
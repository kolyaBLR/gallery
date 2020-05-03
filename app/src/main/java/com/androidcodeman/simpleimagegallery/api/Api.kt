package com.androidcodeman.simpleimagegallery.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("/v2/everything?q=bitcoin&sortBy=publishedAt")
    fun getPosts(@Query("apiKey") key: String = "50b3e01c5daf4c36a0f77fb3144efcf7"): Call<Result>
}
package com.sng.miumap.network

import com.sng.miumap.model.Profile
import retrofit2.Call
import retrofit2.http.GET

interface APIInterface {
    @GET("users/1")
    fun profile(): Call<ResponseWrapper<Profile>>
}
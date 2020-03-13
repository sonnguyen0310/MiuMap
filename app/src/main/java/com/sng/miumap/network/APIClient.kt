package com.sng.miumap.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIClient {
    companion object {
        private const val BASE_URL: String = "https://reqres.in/api/"
        private val client: Retrofit
            get() {
                return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }

        val apiInterface: APIInterface
            get() {
                return client.create(APIInterface::class.java)
            }
    }

}
package com.robotec.caller.speak

import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface JabutiApiService {
    @POST("ask")
    suspend fun ask(
        @Header("X-API-Key") apiKey: String,
        @Query("user_input") userInput: String
    ): Response<Any>
}


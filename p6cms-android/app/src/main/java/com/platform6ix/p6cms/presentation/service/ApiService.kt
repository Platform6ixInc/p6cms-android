package com.platform6ix.p6cms.presentation.service

import com.platform6ix.p6cms.presentation.common.Constants
import com.platform6ix.p6cms.presentation.data.model.FetchRoutesResponseItem
import com.platform6ix.p6cms.presentation.data.model.LoginRequest
import com.platform6ix.p6cms.presentation.data.model.LoginResponse
import com.platform6ix.p6cms.presentation.data.model.RegisterRequest
import com.platform6ix.p6cms.presentation.data.model.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST(Constants.LOGIN_URL)
    suspend fun loginUser(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @POST(Constants.REGISTRATION_URL)
    suspend fun registerUser(
        @Body request: RegisterRequest
    ): Response<RegisterResponse>

    @GET("api/routes")
    suspend fun fetchRoutes(
        @Header("Authorization") authorization: String,
        @Query("route_assigned_to_user")
        userId: Int
    ): Response<List<FetchRoutesResponseItem>>

}
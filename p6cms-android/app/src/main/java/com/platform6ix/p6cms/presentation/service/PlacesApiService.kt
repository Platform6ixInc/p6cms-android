package com.platform6ix.p6cms.presentation.service

import com.platform6ix.p6cms.ui.chargingstations.model.StationsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApiService {
    @GET("maps/api/place/nearbysearch/json?key=AIzaSyAt2c2rI5SG3E6k2Mn8AOgXA8RhEqTESdU")
    suspend fun fetchPlaces(
        @Query("radius")
        radius: Int,
        @Query("location")
        locationString: String,
        @Query("type")
        type: String

    ): Response<StationsResponse>

}
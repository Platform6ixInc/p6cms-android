package com.platform6ix.p6cms.presentation.repo

import com.platform6ix.p6cms.presentation.data.model.FetchRoutesResponseItem
import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.Preferences;
import javax.inject.Inject;


import com.platform6ix.p6cms.presentation.service.ApiService;
import dagger.hilt.android.scopes.ViewModelScoped;
import timber.log.Timber

@ViewModelScoped
class RoutesRepository @Inject constructor(
    private val apiService: ApiService,
    private val preferencesDataStore: DataStore<Preferences>
) {
    suspend fun fetchRoutes(
        authorization: String,
        userId: Int
    ): Result<List<FetchRoutesResponseItem>> {
        return try {

            val response = apiService.fetchRoutes(authorization, userId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Timber.e("RoutesRepository Error fetching routes: ${response.errorBody()?.string()}")
                Result.failure(Exception("Error"))
            }
        } catch (e: Exception) {
            Timber.e("RoutesRepository Exception fetching routes", e)
            Result.failure(e)
        }
    }

}
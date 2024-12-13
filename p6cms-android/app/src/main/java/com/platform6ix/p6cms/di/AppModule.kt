package com.platform6ix.p6cms.di

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import com.platform6ix.p6cms.data.gattble.manager.FR8ProBeaconBleReceiveManager
import com.platform6ix.p6cms.data.gattble.manager.FR8ProBeaconReceiveManager
import com.platform6ix.p6cms.data.routeplannertool.RoutePlannerToolDatabase
import com.platform6ix.p6cms.domain.repository.TaskRoutesRepository
import com.platform6ix.p6cms.domain.repository.TaskRoutesRepositoryImpl
import com.platform6ix.p6cms.domain.repository.TaskStopsRepository
import com.platform6ix.p6cms.domain.repository.TaskStopsRepositoryImpl
import com.platform6ix.p6cms.presentation.common.Constants
import com.platform6ix.p6cms.presentation.service.ApiService
import com.platform6ix.p6cms.presentation.repo.AuthRepository
import com.platform6ix.p6cms.presentation.repo.RoutesRepository
import com.platform6ix.p6cms.presentation.service.PlacesApiService
import com.platform6ix.p6cms.presentation.service.GeocodingApiService
import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import no.nordicsemi.android.kotlin.ble.scanner.BleScanner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

const val USER_PREFERENCES = "user_preferences"

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideBluetoothAdapter(
        @ApplicationContext context: Context
    ): BluetoothAdapter {
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        return bluetoothManager.adapter
    }

    @Provides
    @Singleton
    fun provideFR8ProBeaconReceiveManager(
        @ApplicationContext context: Context,
        bluetoothAdapter: BluetoothAdapter
    ): FR8ProBeaconReceiveManager {
        return FR8ProBeaconBleReceiveManager(bluetoothAdapter, context)
    }

    @Provides
    fun provideBleScanner(
        @ApplicationContext
        context: Context
    ): BleScanner {
        return BleScanner(context)
    }

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class BaseUrlApi

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class BaseUrlChargingStations

    @Provides
    @Singleton
    @BaseUrlApi
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    @BaseUrlChargingStations
    fun provideChargingStationsRetrofit(): Retrofit {
        // Create the logging interceptor
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

        // Create an OkHttpClient and add the logging interceptor
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        // Build and return the Retrofit instance using the OkHttpClient
        return Retrofit.Builder()
            .baseUrl(Constants.GOOGLE_MAPS_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(@BaseUrlApi retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideChargingStationsApiService(@BaseUrlChargingStations retrofit: Retrofit): PlacesApiService =
        retrofit.create(PlacesApiService::class.java)

    @Provides
    @Singleton
    fun provideGeocodingApiService(@BaseUrlChargingStations retrofit: Retrofit): GeocodingApiService =
        retrofit.create(GeocodingApiService::class.java)

    @Provides
    @Singleton
    fun provideAuthRepository(
        apiService: ApiService,
        preferencesDataStore: DataStore<Preferences>
    ): AuthRepository =
        AuthRepository(apiService, preferencesDataStore)

    @Provides
    @Singleton
    fun provideRoutesRepository(
        apiService: ApiService,
        preferencesDataStore: DataStore<Preferences>
    ): RoutesRepository =
        RoutesRepository(apiService, preferencesDataStore)

    // RoutePlannerToolDatabase
    @Singleton
    @Provides
    fun provideRoutePlannerToolDatabase(app: Application): RoutePlannerToolDatabase {
        return Room.databaseBuilder(
            app,
            RoutePlannerToolDatabase::class.java,
            "taskstops.db"
        ).build()
    }

    // TaskStopRepository
    @Singleton
    @Provides
    fun provideTaskStopRepository(db: RoutePlannerToolDatabase): TaskStopsRepository {
        return TaskStopsRepositoryImpl(db.taskStopDao)
    }

    // TaskRouteRepository
    @Singleton
    @Provides
    fun provideTaskRouteRepository(db: RoutePlannerToolDatabase): TaskRoutesRepository {
        return TaskRoutesRepositoryImpl(db.taskRouteDao)
    }

    @Singleton
    @Provides
    fun providePreferencesDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            migrations = listOf(SharedPreferencesMigration(appContext, USER_PREFERENCES)),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { appContext.preferencesDataStoreFile(USER_PREFERENCES) }
        )
    }

    @Singleton
    @Provides
    fun provideFusedLocationProviderClient(@ApplicationContext context: Context): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
}
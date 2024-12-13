package com.platform6ix.p6cms.data.gattble.manager

import com.platform6ix.p6cms.data.gattble.model.FR8ProBeaconResult
import com.platform6ix.p6cms.data.gattble.model.ConnectionState
import com.platform6ix.p6cms.data.gattble.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class MockFR8ProBeaconBleReceiveManager : FR8ProBeaconReceiveManager {

    override val data: MutableSharedFlow<Resource<FR8ProBeaconResult>> = MutableSharedFlow()

    // Create a CoroutineScope for the mock manager
    private val scope = CoroutineScope(Dispatchers.Main)
    override fun startReceiving() {
        // Emit a sample data for preview
        scope.launch {

            data.emit(Resource.Loading(message = "Mock: Scanning for iBeacons..."))

            // Simulate a successful connection with mock data
            data.emit(
                Resource.Success(
                    FR8ProBeaconResult(
                        proximity = 1.5f,             // Mock proximity
                        timestamp = System.currentTimeMillis().toFloat(), // Current timestamp
                        rssi = -70f,                  // Mock RSSI value
                        region = "MockRegion",        // Mock region name
                        accuracy = 2.0f,              // Mock accuracy
                        major = 100f,                 // Mock major value
                        uuid = "123e4567-e89b-12d3-a456-426614174000", // Mock UUID
                        minor = 200f,                 // Mock minor value
                        connectionState = ConnectionState.Connected // Mock connection state
                    )
                )
            )

        }
    }

    override fun reconnect() {
        // No operation for preview
    }

    override fun disconnect() {
        // No operation for preview
    }

    override fun closeConnection() {
        // No operation for preview
    }

    // Include any other methods from the interface that need to be mocked
}

package com.platform6ix.p6cms.data.gattble.model

data class FR8ProBeaconResult(
    val proximity: Float,
    val timestamp: Float,
    val rssi: Float,
    val region: String,
    val accuracy: Float,
    val major: Float,
    val uuid: String,
    val minor: Float,
    val connectionState: ConnectionState
)

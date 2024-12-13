package com.platform6ix.p6cms.data.gattble.manager

import com.platform6ix.p6cms.data.gattble.model.FR8ProBeaconResult
import com.platform6ix.p6cms.data.gattble.util.Resource
import kotlinx.coroutines.flow.MutableSharedFlow

interface FR8ProBeaconReceiveManager {
    val data: MutableSharedFlow<Resource<FR8ProBeaconResult>>

    fun reconnect()

    fun disconnect()

    fun startReceiving()

    fun closeConnection()

}
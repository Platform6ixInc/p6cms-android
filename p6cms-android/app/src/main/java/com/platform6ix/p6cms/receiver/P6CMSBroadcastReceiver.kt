package com.platform6ix.p6cms.Receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.platform6ix.p6cms.services.BeaconProToolsService


class P6CMSBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val startServiceIntent = Intent(context, BeaconProToolsService::class.java)
        context.startService(startServiceIntent)
    }
}
package com.platform6ix.p6cms.data.kotlinble.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.platform6ix.p6cms.data.kotlinble.util.BlePermissionNotAvailableReason
import com.platform6ix.p6cms.data.kotlinble.util.BlePermissionState
import com.platform6ix.p6cms.data.kotlinble.viewmodel.PermissionViewModel

@Composable
fun RequireLocation(
    onChanged: (Boolean) -> Unit = {},
    contentWithoutLocation: @Composable () -> Unit = { LocationPermissionRequiredView() },
    content: @Composable (isLocationRequiredAndDisabled: Boolean) -> Unit,
) {
    val viewModel = hiltViewModel<PermissionViewModel>()
    val state by viewModel.locationPermission.collectAsStateWithLifecycle()

    LaunchedEffect(state) {
        onChanged(state is BlePermissionState.Available || (state as BlePermissionState.NotAvailable).reason == BlePermissionNotAvailableReason.DISABLED)
    }

    when (val s = state) {
        is BlePermissionState.NotAvailable -> when (s.reason) {
            BlePermissionNotAvailableReason.DISABLED -> content(true)
            else -> contentWithoutLocation()
        }

        BlePermissionState.Available -> content(false)
    }
}
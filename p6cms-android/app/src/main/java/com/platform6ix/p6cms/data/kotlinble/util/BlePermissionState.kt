package com.platform6ix.p6cms.data.kotlinble.util

enum class BlePermissionNotAvailableReason {
    PERMISSION_REQUIRED,
    NOT_AVAILABLE,
    DISABLED,
}

sealed class BlePermissionState {
    object Available : BlePermissionState()
    data class NotAvailable(
        val reason: BlePermissionNotAvailableReason,
    ) : BlePermissionState()
}

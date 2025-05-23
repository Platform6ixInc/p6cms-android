package com.platform6ix.p6cms.data.kotlinble.view

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.BluetoothSearching
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.platform6ix.p6cms.ui.theme.Xlr8ProTheme
import com.platform6ix.p6cms.R
import no.nordicsemi.android.common.core.parseBold

@Composable
internal fun ScanEmptyView(
    requireLocation: Boolean,
) {
    WarningView(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        imageVector = Icons.AutoMirrored.Filled.BluetoothSearching,
        title = stringResource(id = R.string.no_device_guide_title),
        hint = stringResource(id = R.string.no_device_guide_info) + if (requireLocation) {
            "\n\n" + stringResource(id = R.string.no_device_guide_location_info)
        } else {
            ""
        }.parseBold(),
        hintTextAlign = TextAlign.Justify,
    ) {
        if (requireLocation) {
            val context = LocalContext.current
            Button(onClick = { openLocationSettings(context) }) {
                Text(text = stringResource(id = R.string.action_location_settings))
            }
        }
    }
}

private fun openLocationSettings(context: Context) {
    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent)
}

@Preview
@Composable
private fun ScanEmptyViewPreview_RequiredLocation() {
    Xlr8ProTheme {
        ScanEmptyView(
            requireLocation = true,
        )
    }
}

@Preview(device = Devices.TABLET)
@Composable
private fun ScanEmptyViewPreview() {
    Xlr8ProTheme {
        ScanEmptyView(
            requireLocation = false,
        )
    }
}
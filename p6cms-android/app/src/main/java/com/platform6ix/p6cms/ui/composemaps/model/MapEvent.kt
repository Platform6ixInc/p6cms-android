package com.platform6ix.p6cms.ui.composemaps.model

import com.platform6ix.p6cms.domain.model.taskstop.TaskStop
import com.google.android.gms.maps.model.LatLng

sealed class MapEvent {
    object ToggleFalloutMap : MapEvent()
    data class onMapClick(val latLng: LatLng): MapEvent()
    data class OnMapLongClick(val latLng: LatLng): MapEvent()
    data class OnInfoWindowLongClick(val taskStop: TaskStop): MapEvent()

}

package com.platform6ix.p6cms.ui.zonecomposemaps.model

import com.platform6ix.p6cms.ui.zonecomposemaps.clusters.ZoneClusterItem
import android.location.Location

data class ZoneMapState(
    val lastKnownLocation: Location?,
    val clusterItems: List<ZoneClusterItem>,
)
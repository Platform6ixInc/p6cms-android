package com.platform6ix.p6cms.data.routeplannertool

import com.platform6ix.p6cms.domain.model.taskstop.TaskStop

fun TaskStopEntity.toTaskStop(): TaskStop {
    return TaskStop(
        title = title,
        description = description,
        formattedAddressOrigin = formattedAddressOrigin,
        latOrigin = latOrigin,
        lngOrigin = lngOrigin,
        formattedAddressDestination = formattedAddressDestination,
        latDestination = latDestination,
        lngDestination = lngDestination,
        id = id
    )
}

fun TaskStop.toTaskStopEntity(): TaskStopEntity {
    return TaskStopEntity(
        title = title,
        description = description,
        formattedAddressOrigin = formattedAddressOrigin,
        latOrigin = latOrigin,
        lngOrigin = lngOrigin,
        formattedAddressDestination = formattedAddressDestination,
        latDestination = latDestination,
        lngDestination = lngDestination,
        id = id
    )
}
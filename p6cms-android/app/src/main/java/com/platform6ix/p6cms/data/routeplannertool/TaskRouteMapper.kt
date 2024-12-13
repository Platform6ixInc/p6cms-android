package com.platform6ix.p6cms.data.routeplannertool

import com.platform6ix.p6cms.domain.model.taskstop.TaskRoute


fun TaskRouteEntity.toTaskRoute(): TaskRoute {
    return TaskRoute(
        title = title,
        notes = notes,
        stops = stops,
        id = id
    )
}

fun TaskRoute.toTaskRouteEntity(): TaskRouteEntity {
    return TaskRouteEntity(
        title = title,
        notes = notes,
        stops = stops,
        id = id
    )
}
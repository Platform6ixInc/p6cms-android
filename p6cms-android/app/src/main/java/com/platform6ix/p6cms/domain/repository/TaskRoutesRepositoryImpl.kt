package com.platform6ix.p6cms.domain.repository

import com.platform6ix.p6cms.data.routeplannertool.TaskRouteDao
import com.platform6ix.p6cms.data.routeplannertool.TaskStopDao
import com.platform6ix.p6cms.data.routeplannertool.toTaskRoute
import com.platform6ix.p6cms.data.routeplannertool.toTaskRouteEntity
import com.platform6ix.p6cms.data.routeplannertool.toTaskStop
import com.platform6ix.p6cms.data.routeplannertool.toTaskStopEntity
import com.platform6ix.p6cms.domain.model.taskstop.TaskRoute
import com.platform6ix.p6cms.domain.model.taskstop.TaskStop
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRoutesRepositoryImpl(
    private val taskRouteDao: TaskRouteDao
): TaskRoutesRepository {
    override suspend fun insertTaskRoute(taskRoute: TaskRoute) {
        taskRouteDao.insertTaskRoute(taskRoute.toTaskRouteEntity())
    }

    override suspend fun deleteTaskRoute(taskRoute: TaskRoute) {
        taskRouteDao.deleteTaskRoute(taskRoute.toTaskRouteEntity())
    }

    override fun getTaskRoutes(): Flow<List<TaskRoute>> {
        return taskRouteDao.getTaskRoutes().map { taskRoutes ->
            taskRoutes.map {it.toTaskRoute()}
        }
    }
}
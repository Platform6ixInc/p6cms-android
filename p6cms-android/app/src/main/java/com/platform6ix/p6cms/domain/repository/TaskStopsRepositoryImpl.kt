package com.platform6ix.p6cms.domain.repository

import com.platform6ix.p6cms.data.routeplannertool.TaskStopDao
import com.platform6ix.p6cms.data.routeplannertool.toTaskStop
import com.platform6ix.p6cms.data.routeplannertool.toTaskStopEntity
import com.platform6ix.p6cms.domain.model.taskstop.TaskStop
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskStopsRepositoryImpl(
    private val taskStopDao: TaskStopDao
): TaskStopsRepository {
    override suspend fun insertTaskStop(taskStop: TaskStop) {
        taskStopDao.insertTaskStop(taskStop.toTaskStopEntity())
    }

    override suspend fun deleteTaskStop(taskStop: TaskStop) {
        // TODO("Not yet implemented")
        taskStopDao.deleteTaskStop(taskStop.toTaskStopEntity())
    }

    override fun getTaskStops(): Flow<List<TaskStop>> {
        // TODO("Not yet implemented")
        return taskStopDao.getTaskStops().map { taskStops ->
            taskStops.map {it.toTaskStop()}
        }
    }
}
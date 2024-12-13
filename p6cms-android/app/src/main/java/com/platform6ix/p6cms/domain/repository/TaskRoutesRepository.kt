package com.platform6ix.p6cms.domain.repository

import com.platform6ix.p6cms.domain.model.taskstop.TaskRoute
import kotlinx.coroutines.flow.Flow

interface TaskRoutesRepository {
    suspend fun insertTaskRoute(taskStop: TaskRoute)

    suspend fun deleteTaskRoute(taskStop: TaskRoute)

    fun getTaskRoutes(): Flow<List<TaskRoute>>
}
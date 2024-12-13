package com.platform6ix.p6cms.domain.repository

import com.platform6ix.p6cms.domain.model.taskstop.TaskStop
import kotlinx.coroutines.flow.Flow

interface TaskStopsRepository {

    suspend fun insertTaskStop(taskStop: TaskStop)

    suspend fun deleteTaskStop(taskStop: TaskStop)

    fun getTaskStops(): Flow<List<TaskStop>>
}
package ru.quipy.logic

import ru.quipy.api.StatusChangedEvent
import ru.quipy.api.StatusCreatedEvent
import ru.quipy.config.Services
import java.util.*

fun StatusAggregateState.create(
    id: UUID, name: String, projectId: UUID, taskId: UUID,
    services: Services
): StatusCreatedEvent {
    if (services.projectEsService.getState(projectId) == null) {
        throw IllegalArgumentException("No project with id $projectId")
    }

    if (services.taskEsService.getState(taskId) == null) {
        throw IllegalArgumentException("No task with id $taskId")
    }

    return StatusCreatedEvent(
        statusId = id,
        statusName = name,
        projectId = projectId,
        taskId = taskId,
    )
}

fun StatusAggregateState.update(id: UUID, name: String, services: Services): StatusChangedEvent {

    if (services.statusEsService.getState(id) == null) {
        throw IllegalArgumentException("No project with id $id")
    }
    return StatusChangedEvent(
        statusId = id,
        statusName = name,
    )
}
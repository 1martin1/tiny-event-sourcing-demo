package ru.quipy.logic

import ru.quipy.api.ProjectCreatedEvent
import ru.quipy.api.StatusChangedEvent
import ru.quipy.api.StatusCreatedEvent
import java.util.*

fun StatusAggregateState.create(id: UUID, name: String, projectId: UUID, taskId: UUID): StatusCreatedEvent {
    return StatusCreatedEvent(
        statusId = id,
        statusName = name,
        projectId = projectId,
        taskId = taskId,
    )
}

fun StatusAggregateState.update(id: UUID, name: String): StatusChangedEvent {
    return StatusChangedEvent(
        statusId = id,
        statusName = name,
    )
}
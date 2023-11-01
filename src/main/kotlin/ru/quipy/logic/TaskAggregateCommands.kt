package ru.quipy.logic

import ru.quipy.api.StatusChangedEvent
import ru.quipy.api.StatusCreatedEvent
import ru.quipy.api.TaskCreatedEvent
import java.util.*

fun TaskAggregateState.create(id: UUID, name: String, projectId: UUID, executors: List<UUID>): TaskCreatedEvent {
    return TaskCreatedEvent(
        taskId = id,
        projectId = projectId,
        taskName = name,
        executors = executors,
    )
}

package ru.quipy.logic

import ru.quipy.api.*
import java.util.*

fun TaskAggregateState.create(id: UUID, name: String, projectId: UUID, executors: List<UUID>): TaskCreatedEvent {
    return TaskCreatedEvent(
        taskId = id,
        projectId = projectId,
        taskName = name,
        executors = executors,
    )
}

fun TaskAggregateState.rename(id: UUID, name: String): TaskRenamedEvent {
    return TaskRenamedEvent(
        taskId = id,
        taskName = name,
    )
}

fun TaskAggregateState.setExecutors(id: UUID, executors: List<UUID>): ExecutorsChangedEvent {
    return ExecutorsChangedEvent(
        taskId = id,
        executors = executors,
    )
}

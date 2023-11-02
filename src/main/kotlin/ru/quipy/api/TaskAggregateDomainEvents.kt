package ru.quipy.api

import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import java.util.*

const val TASK_CREATED_EVENT = "TASK_CREATED_EVENT"
const val TASK_RENAMED_EVENT = "TASK_RENAMED_EVENT"
const val EXECUTOR_CHANGED_EVENT = "EXECUTOR_CHANGED_EVENT"

@DomainEvent(name = TASK_CREATED_EVENT)
class TaskCreatedEvent(
    val taskId: UUID,
    val projectId: UUID,
    val taskName: String,
    val executors: List<UUID>,
    createdAt: Long = System.currentTimeMillis(),
) : Event<TaskAggregate>(
    name = TASK_CREATED_EVENT,
    createdAt = createdAt
)

@DomainEvent(name = TASK_RENAMED_EVENT)
class TaskRenamedEvent(
    val taskId: UUID,
    val taskName: String,
    createdAt: Long = System.currentTimeMillis(),
) : Event<TaskAggregate>(
    name = TASK_RENAMED_EVENT,
    createdAt = createdAt
)

@DomainEvent(name = EXECUTOR_CHANGED_EVENT)
class ExecutorsChangedEvent(
    val taskId: UUID,
    val executors: List<UUID>,
    createdAt: Long = System.currentTimeMillis(),
) : Event<TaskAggregate>(
    name = EXECUTOR_CHANGED_EVENT,
    createdAt = createdAt
)
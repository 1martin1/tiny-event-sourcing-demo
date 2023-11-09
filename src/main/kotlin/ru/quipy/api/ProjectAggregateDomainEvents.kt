package ru.quipy.api

import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import java.util.*

const val PROJECT_CREATED_EVENT = "PROJECT_CREATED_EVENT"
const val MEMBER_ADDED_EVENT = "MEMBER_ADDED_EVENT"
const val TASK_ADDED_EVENT = "TASK_ADDED_EVENT"
const val STATUS_ADDED_EVENT = "STATUS_ADDED_EVENT"

// API
@DomainEvent(name = PROJECT_CREATED_EVENT)
class ProjectCreatedEvent(
    val projectId: UUID,
    val title: String,
    val creatorId: UUID,
    createdAt: Long = System.currentTimeMillis(),
) : Event<ProjectAggregate>(
    name = PROJECT_CREATED_EVENT,
    createdAt = createdAt,
)

@DomainEvent(name = MEMBER_ADDED_EVENT)
class MemberAddedEvent(
    val projectId: UUID,
    var userId: UUID,
    createdAt: Long = System.currentTimeMillis(),
) : Event<ProjectAggregate>(
    name = MEMBER_ADDED_EVENT,
    createdAt = createdAt,
)

@DomainEvent(name = TASK_ADDED_EVENT)
class TaskAddedEvent(
    val taskCreatedEvent: TaskCreatedEvent,
    createdAt: Long = System.currentTimeMillis(),
) : Event<ProjectAggregate>(
    name = TASK_ADDED_EVENT,
    createdAt = createdAt,
)

@DomainEvent(name = STATUS_ADDED_EVENT)
class StatusAddedEvent(
    val statusCreatedEvent: StatusCreatedEvent,
    createdAt: Long = System.currentTimeMillis(),
) : Event<ProjectAggregate>(
    name = STATUS_ADDED_EVENT,
    createdAt = createdAt,
)
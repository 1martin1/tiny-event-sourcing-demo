package ru.quipy.api

import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import java.util.*

const val STATUS_CHANGED_EVENT = "STATUS_CHANGED_EVENT"
const val STATUS_CREATED_EVENT = "STATUS_CREATED_EVENT"

@DomainEvent(name = STATUS_CREATED_EVENT)
class StatusCreatedEvent(
    val statusId: UUID,
    val statusName: String,
    val projectId: UUID,
    val taskId: UUID,
    createdAt: Long = System.currentTimeMillis(),
) : Event<StatusAggregate>(
    name = STATUS_CREATED_EVENT,
    createdAt = createdAt
)
@DomainEvent(name = STATUS_CHANGED_EVENT)
class StatusChangedEvent(
    val statusId: UUID,
    val statusName: String,
    createdAt: Long = System.currentTimeMillis(),
) : Event<StatusAggregate>(
    name = STATUS_CHANGED_EVENT,
    createdAt = createdAt
)
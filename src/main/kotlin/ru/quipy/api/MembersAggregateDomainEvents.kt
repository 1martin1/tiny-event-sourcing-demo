package ru.quipy.api

import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import java.util.*

const val MEMBER_ADDED_EVENT = "MEMBER_ADDED_EVENT"
const val MEMBERS_CREATED_EVENT = "MEMBERS_CREATED_EVENT"

@DomainEvent(name = MEMBER_ADDED_EVENT)
class MemberAddedEvent(
    val membersId: UUID,
    val userId: UUID,
    createdAt: Long = System.currentTimeMillis(),
) : Event<MembersAggregate>(
    name = MEMBER_ADDED_EVENT,
    createdAt = createdAt
)

@DomainEvent(name = MEMBERS_CREATED_EVENT)
class MembersCreatedEvent(
    val membersId: UUID,
    val projectId: UUID,
    createdAt: Long = System.currentTimeMillis(),
) : Event<MembersAggregate>(
    name = MEMBERS_CREATED_EVENT,
    createdAt = createdAt
)
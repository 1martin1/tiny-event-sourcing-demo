package ru.quipy.logic

import ru.quipy.api.MemberAddedEvent
import ru.quipy.api.MembersAggregate
import ru.quipy.api.MembersCreatedEvent
import ru.quipy.core.annotations.StateTransitionFunc
import ru.quipy.domain.AggregateState
import java.util.*


class MembersAggregateState : AggregateState<UUID, MembersAggregate> {

    private lateinit var membersId: UUID
    private lateinit var projectId: UUID
    private lateinit var members: MutableList<UUID>

    var createdAt: Long = System.currentTimeMillis()
    var updatedAt: Long = System.currentTimeMillis()


    override fun getId() = membersId

    // State transition functions which is represented by the class member function

    @StateTransitionFunc
    fun membersCreatedApply(event: MembersCreatedEvent) {
        membersId = event.membersId
        projectId = event.projectId

        updatedAt = createdAt
    }

    @StateTransitionFunc
    fun memberAddedApply(event: MemberAddedEvent) {
        members.add(event.userId)

        updatedAt = createdAt
    }

}
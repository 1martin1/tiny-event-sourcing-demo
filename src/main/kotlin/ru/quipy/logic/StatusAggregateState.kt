package ru.quipy.logic

import ru.quipy.api.*
import ru.quipy.core.annotations.StateTransitionFunc
import ru.quipy.domain.AggregateState
import java.util.*

class StatusAggregateState : AggregateState<UUID, StatusAggregate> {
    private lateinit var statusId: UUID
    private lateinit var projecrId: UUID
    private lateinit var taskId: UUID
    var createdAt: Long = System.currentTimeMillis()
    var updatedAt: Long = System.currentTimeMillis()

    lateinit var name: String

    override fun getId() = statusId

    // State transition functions which is represented by the class member function

    @StateTransitionFunc
    fun statusCreatedApply(event: StatusCreatedEvent) {
        statusId = event.statusId;
        name = event.statusName
        projecrId = event.projectId
        taskId = event.taskId
        updatedAt = createdAt
    }
    @StateTransitionFunc
    fun statusChangedApply(event: StatusChangedEvent) {
        statusId = event.statusId;
        name = event.statusName
        updatedAt = createdAt
    }
}
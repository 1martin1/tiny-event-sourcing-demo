package ru.quipy.logic

import ru.quipy.api.*
import ru.quipy.core.annotations.StateTransitionFunc
import ru.quipy.domain.AggregateState
import java.util.*

// Service's business logic
class ProjectAggregateState : AggregateState<UUID, ProjectAggregate> {
    private lateinit var projectId: UUID
    var createdAt: Long = System.currentTimeMillis()
    var updatedAt: Long = System.currentTimeMillis()

    var tasks: MutableList<UUID> = mutableListOf()
    var members: MutableList<UUID> = mutableListOf()
    var statuses: MutableList<UUID> = mutableListOf()
    var membersCount : Int = 0

    lateinit var creatorId: UUID
    lateinit var title: String



    override fun getId() = projectId

    // State transition functions which is represented by the class member function
    @StateTransitionFunc
    fun projectCreatedApply(event: ProjectCreatedEvent) {
        projectId = event.projectId
        title = event.title
        creatorId = event.creatorId
        members.add(event.creatorId)
        membersCount = 1

        updatedAt = createdAt
    }

    @StateTransitionFunc
    fun memberAddedApply(event: MemberAddedEvent) {
        members.add(event.userId)
        ++membersCount;

        updatedAt = createdAt
    }

    @StateTransitionFunc
    fun taskCreatedApply(event: TaskAddedEvent) {
        tasks.add(event.taskCreatedEvent.taskId)

        updatedAt = System.currentTimeMillis()
    }

    @StateTransitionFunc
    fun StatusCreatedApply(event: StatusAddedEvent) {
        statuses.add(event.statusCreatedEvent.statusId)

        updatedAt = System.currentTimeMillis()
    }


}

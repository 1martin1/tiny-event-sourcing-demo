package ru.quipy.logic

import ru.quipy.api.ExecutorsChangedEvent
import ru.quipy.api.TaskAggregate
import ru.quipy.api.TaskCreatedEvent
import ru.quipy.api.TaskRenamedEvent
import ru.quipy.core.annotations.StateTransitionFunc
import ru.quipy.domain.AggregateState
import java.util.*

class TaskAggregateState : AggregateState<UUID, TaskAggregate> {
    private lateinit var taskId: UUID
    lateinit var taskName: String
    lateinit var executors: List<UUID>
    lateinit var projectId: UUID
    var createdAt: Long = System.currentTimeMillis()
    var updatedAt: Long = System.currentTimeMillis()


    override fun getId() = taskId

    // State transition functions which is represented by the class member function

    @StateTransitionFunc
    fun taskCreatedApply(event: TaskCreatedEvent) {
        taskId = event.taskId;
        taskName = event.taskName
        executors = event.executors
        projectId = event.projectId

        updatedAt = createdAt
    }

    @StateTransitionFunc
    fun taskRenamedApply(event: TaskRenamedEvent) {
        taskName = event.taskName

        updatedAt = createdAt
    }

    @StateTransitionFunc
    fun executorsChangedApply(event: ExecutorsChangedEvent) {
        executors = event.executors

        updatedAt = createdAt
    }
}
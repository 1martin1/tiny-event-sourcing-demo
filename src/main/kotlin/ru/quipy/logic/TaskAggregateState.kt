package ru.quipy.logic

import ru.quipy.api.*
import ru.quipy.core.annotations.StateTransitionFunc
import ru.quipy.domain.AggregateState
import java.util.*

class TaskAggregateState : AggregateState<UUID, TaskAggregate> {
    private lateinit var taskId: UUID
    private lateinit var taskName: String
    private lateinit var executors: List<UUID>
    var createdAt: Long = System.currentTimeMillis()
    var updatedAt: Long = System.currentTimeMillis()


    override fun getId() = taskId

    // State transition functions which is represented by the class member function

    @StateTransitionFunc
    fun taskCreatedApply(event: TaskCreatedEvent) {
        taskId = event.taskId;
        taskName = event.taskName
        executors = event.executors

        updatedAt = createdAt
    }

    @StateTransitionFunc
    fun taskRenamedApply(event: TaskRenamedEvent) {
        taskId = event.taskId;
        taskName = event.taskName

        updatedAt = createdAt
    }

    @StateTransitionFunc
    fun executorsChangedApply(event: ExecutorsChangedEvent) {
        taskId = event.taskId;
        executors = event.executors

        updatedAt = createdAt
    }
}
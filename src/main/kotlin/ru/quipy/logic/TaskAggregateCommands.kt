package ru.quipy.logic

import ru.quipy.api.ExecutorsChangedEvent
import ru.quipy.api.TaskCreatedEvent
import ru.quipy.api.TaskRenamedEvent
import ru.quipy.config.Services
import java.util.*

fun TaskAggregateState.create(
    id: UUID, name: String, projectId: UUID, executors: List<UUID>,
    services: Services
): TaskCreatedEvent {

    if (services.projectEsService.getState(projectId) == null) {
        throw IllegalArgumentException("No project with id $projectId")
    }
    executors.forEach { executorId ->
        if (services.projectEsService.getState(projectId)!!.members.find { it == executorId } == null) {
            throw IllegalArgumentException("No members with id $executorId")
        }
    }

    if (services.taskProjection.taskIds.any {
            services.taskEsService.getState(it)!!.projectId == projectId && services.taskEsService.getState(
                it
            )!!.taskName == taskName
        }) {
        throw IllegalArgumentException("Task $id already exists")

    }

    println("OK1")
    return TaskCreatedEvent(
        taskId = id,
        projectId = projectId,
        taskName = name,
        executors = executors,
    )
}

fun TaskAggregateState.rename(
    id: UUID, name: String,
    services: Services
): TaskRenamedEvent {
    if (services.taskEsService.getState(id) == null) {
        throw IllegalArgumentException("No task with id $id")
    }

    return TaskRenamedEvent(
        taskId = id,
        taskName = name,
    )
}

fun TaskAggregateState.setExecutors(
    id: UUID, executors: List<UUID>,
    services: Services
): ExecutorsChangedEvent {
    if (services.taskEsService.getState(id) == null) {
        throw IllegalArgumentException("No task with id $id")
    }
    executors.forEach { executorId ->
        if (services.userEsService.getState(executorId) == null) {
            throw IllegalArgumentException("No user with id $executorId")
        }
    }
    executors.forEach { executorId ->
        if (services.taskEsService.getState(id)!!.executors.any { it == executorId }) {
            throw IllegalArgumentException("User $executorId already in task")
        }
    }
    executors.forEach { executorId ->
        if (executors.any { it == executorId }) {
            throw IllegalArgumentException("User $executorId with same id")
        }
    }


    return ExecutorsChangedEvent(
        taskId = id,
        executors = executors,
    )
}

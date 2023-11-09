package ru.quipy.logic

import ru.quipy.api.*
import ru.quipy.config.Services
import java.util.*


// Commands : takes something -> returns event
// Here the commands are represented by extension functions, but also can be the class member functions

fun ProjectAggregateState.create(id: UUID, title: String, creatorId: UUID, services: Services): ProjectCreatedEvent {
    if (services.projectProjection.projectIds.any { services.projectEsService.getState(it)!!.title == title }) {
        throw IllegalArgumentException("Project $title already exists")
    }
    if (services.userEsService.getState(creatorId) == null) {
        throw IllegalArgumentException("No user $creatorId")
    }
    return ProjectCreatedEvent(
        projectId = id,
        title = title,
        creatorId = creatorId,
    )
}

fun ProjectAggregateState.addMember(projectId: UUID, userId: UUID, services: Services): MemberAddedEvent {
    if (services.projectEsService.getState(projectId) == null) {
        throw IllegalArgumentException("No project with id $projectId")
    }
    return MemberAddedEvent(
        projectId = projectId,
        userId = userId,
    )
}

fun ProjectAggregateState.addTask(taskCreatedEvent: TaskCreatedEvent, services: Services): TaskAddedEvent {
    if (services.projectEsService.getState(taskCreatedEvent.projectId) == null) {
        throw IllegalArgumentException("No project with id ${taskCreatedEvent.projectId}")
    }
    return TaskAddedEvent(
        taskCreatedEvent = taskCreatedEvent
    )
}

fun ProjectAggregateState.addStatus(statusCreatedEvent: StatusCreatedEvent, services: Services): StatusAddedEvent {
    if (services.projectEsService.getState(statusCreatedEvent.projectId) == null) {
        throw IllegalArgumentException("No project with id ${statusCreatedEvent.projectId}")
    }
    return StatusAddedEvent(
        statusCreatedEvent = statusCreatedEvent
    )
}
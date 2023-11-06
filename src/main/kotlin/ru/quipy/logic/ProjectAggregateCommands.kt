package ru.quipy.logic

import ru.quipy.api.MemberAddedEvent
import ru.quipy.api.ProjectCreatedEvent
import ru.quipy.config.Services
import java.util.*


// Commands : takes something -> returns event
// Here the commands are represented by extension functions, but also can be the class member functions

fun ProjectAggregateState.create(id: UUID, title: String, creatorId: UUID, services: Services): ProjectCreatedEvent {
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

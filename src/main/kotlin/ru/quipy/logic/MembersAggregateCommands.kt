package ru.quipy.logic

import ru.quipy.api.MemberAddedEvent
import ru.quipy.api.MembersCreatedEvent
import ru.quipy.config.Services
import java.util.*


fun MembersAggregateState.create(
    id: UUID, projectId: UUID,
    services: Services
): MembersCreatedEvent {


    if (services.projectEsService.getState(projectId) == null) {
        throw IllegalArgumentException("No project with id $projectId")
    }

    return MembersCreatedEvent(
        membersId = id,
        projectId = projectId,
    )
}

fun MembersAggregateState.addMember(
    membersId: UUID, userId: UUID,
    services: Services
): MemberAddedEvent {

    if (services.membersEsService.getState(membersId) == null) {
        throw IllegalArgumentException("No members with id $membersId")
    }
    if (services.userEsService.getState(userId) == null) {
        throw IllegalArgumentException("No user with id $userId")
    }

    return MemberAddedEvent(
        membersId = membersId,
        userId = userId,
    )
}

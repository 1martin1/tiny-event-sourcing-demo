package ru.quipy.logic

import ru.quipy.api.UserCreatedEvent
import ru.quipy.config.Services
import java.util.*


// Commands : takes something -> returns event
// Here the commands are represented by extension functions, but also can be the class member functions

fun UserAggregateState.create(
    id: UUID, nickname: String, name: String, password: String,
    services: Services
): UserCreatedEvent {
    if (services.userProjection.userIds.any { services.userEsService.getState(it)!!.nickname == nickname }) {
        throw IllegalArgumentException("User $nickname already exists")
    }
    return UserCreatedEvent(
        userId = id,
        nickname = nickname,
        username = name,
        password = password,
    )
}
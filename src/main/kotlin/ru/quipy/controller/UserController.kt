package ru.quipy.controller

import org.springframework.web.bind.annotation.*
import ru.quipy.api.UserCreatedEvent
import ru.quipy.config.Services
import ru.quipy.logic.UserAggregateState
import ru.quipy.logic.create
import java.util.*

@RestController
@RequestMapping("/users")
class UserController(
    val services: Services
) {

    @PostMapping("/createName")
    fun createUser(@RequestParam nickname: String, @RequestParam name: String, @RequestParam password: String) : UserCreatedEvent {
        return services.userEsService.create { it.create(UUID.randomUUID(), nickname, name, password, services) }
    }


    // Этого тут быть не должно
    // не ивент сорсинг
    @GetMapping("/{userId}")
    fun getUser(@PathVariable userId: UUID) : UserAggregateState? {
        return services.userEsService.getState(userId);
    }

    @GetMapping("/users")
    fun getUsers() : List<UUID> {
        return services.userProjection.userIds
    }

    @PostMapping("/users/exists")
    fun ifExists(@RequestParam login: String) : Boolean {
        return services.userProjection.userLogins.containsValue(login);
    }

    @GetMapping("/{userId}/projects")
    fun getProjects(@PathVariable userId: UUID) : List<UUID>? {
        return services.userProjection.userProjects[userId]
    }

}
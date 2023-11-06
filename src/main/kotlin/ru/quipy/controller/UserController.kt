package ru.quipy.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.quipy.api.UserAggregate
import ru.quipy.api.UserCreatedEvent
import ru.quipy.config.Services
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.ProjectAggregateState
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
        return services.userEsService.create { it.create(UUID.randomUUID(), nickname, name, password) }
    }


    // Этого тут быть не должно
    // не ивент сорсинг
    @GetMapping("/{userId}")
    fun getUser(@PathVariable userId: UUID) : UserAggregateState? {
        return services.userEsService.getState(userId);
    }

}
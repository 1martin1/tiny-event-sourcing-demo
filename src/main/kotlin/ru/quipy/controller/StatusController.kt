package ru.quipy.controller

import org.springframework.web.bind.annotation.*
import ru.quipy.api.*
import ru.quipy.config.Services
import ru.quipy.logic.create
import ru.quipy.logic.update
import java.util.*

@RestController
@RequestMapping("/statuses")
class StatusController(
    val services: Services
) {
    @PostMapping("/{statusName}")
    fun createStatus(@PathVariable statusName: String, @RequestParam projectId: UUID, taskId: UUID) : StatusCreatedEvent {
        return services.statusEsService.create { it.create(UUID.randomUUID(), statusName, projectId, taskId, services) }
    }

    @PostMapping("/{statusId}")
    fun changeStatus(@PathVariable statusId: UUID, @RequestParam taskName: String) : StatusChangedEvent {
        return services.statusEsService.create { it.update(statusId, taskName, services) }
    }
}
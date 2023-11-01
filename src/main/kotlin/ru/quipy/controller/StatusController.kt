package ru.quipy.controller

import org.springframework.web.bind.annotation.*
import ru.quipy.api.*
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.ProjectAggregateState
import ru.quipy.logic.StatusAggregateState
import ru.quipy.logic.create
import ru.quipy.logic.update
import java.util.*

@RestController
@RequestMapping("/statuses")
class StatusController(
    val statusEsService: EventSourcingService<UUID, StatusAggregate, StatusAggregateState>
) {
    @PostMapping("/{statusName}")
    fun createTask(@PathVariable statusName: String, @RequestParam projectId: UUID, taskId: UUID) : StatusCreatedEvent {
        return statusEsService.create { it.create(UUID.randomUUID(), statusName, projectId, taskId) }
    }

    @PostMapping("/{statusID}")
    fun changeStatus(@PathVariable statusId: UUID, @RequestParam taskName: String) : StatusChangedEvent {
        return statusEsService.create { it.update(statusId, taskName) }
    }
}
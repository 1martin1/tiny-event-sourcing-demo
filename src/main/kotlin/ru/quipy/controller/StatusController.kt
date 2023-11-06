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
    val projectEsService: EventSourcingService<UUID, ProjectAggregate, ProjectAggregateState>,
    val statusEsService: EventSourcingService<UUID, StatusAggregate, StatusAggregateState>
) {
    @PostMapping("/{statusName}")
    fun createStatus(@PathVariable statusName: String, @RequestParam projectId: UUID, taskId: UUID) : StatusCreatedEvent {
        if (projectEsService.getState(projectId) == null) {
            throw IllegalArgumentException("No project with id $projectId")
        }
        return statusEsService.create { it.create(UUID.randomUUID(), statusName, projectId, taskId) }
    }

    @PostMapping("/{statusId}")
    fun changeStatus(@PathVariable statusId: UUID, @RequestParam taskName: String) : StatusChangedEvent {
        if (statusEsService.getState(statusId) == null) {
            throw IllegalArgumentException("No status with id $statusId")
        }
        return statusEsService.create { it.update(statusId, taskName) }
    }
}
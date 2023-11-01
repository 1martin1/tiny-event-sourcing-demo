package ru.quipy.controller

import org.springframework.web.bind.annotation.*
import ru.quipy.api.*
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.StatusAggregateState
import ru.quipy.logic.TaskAggregateState
import ru.quipy.logic.create
import ru.quipy.logic.update
import java.util.*

@RestController
@RequestMapping("/tasks")
class TaskController(
    val taskEsService: EventSourcingService<UUID, TaskAggregate, TaskAggregateState>
) {
    @PostMapping("/{taskName}")
    fun createTask(@PathVariable taskName: String, @RequestParam projectId: UUID, @RequestParam executors: List<UUID>) : TaskCreatedEvent {
        return taskEsService.create { it.create(UUID.randomUUID(), taskName, projectId, executors) }
    }

}
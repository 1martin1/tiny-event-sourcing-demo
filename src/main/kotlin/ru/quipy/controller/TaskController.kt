package ru.quipy.controller

import org.springframework.web.bind.annotation.*
import ru.quipy.api.*
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.*
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

    @PostMapping("/{taskId}")
    fun renameTask(@PathVariable taskId: UUID, @RequestParam taskName: String) : TaskRenamedEvent {
        return taskEsService.update(taskId) { it.rename(taskId, taskName) }
    }

    @PostMapping("/{taskId}")
    fun setExecutors(@PathVariable taskId: UUID, @RequestParam executors: List<UUID>) : ExecutorsChangedEvent {
        return taskEsService.update(taskId) { it.setExecutors(taskId, executors) }
    }

}
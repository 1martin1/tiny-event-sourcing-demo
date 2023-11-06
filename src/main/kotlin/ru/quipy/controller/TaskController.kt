package ru.quipy.controller

import org.springframework.web.bind.annotation.*
import ru.quipy.api.*
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.*
import java.util.*

@RestController
@RequestMapping("/tasks")
class TaskController(
    val taskEsService: EventSourcingService<UUID, TaskAggregate, TaskAggregateState>,
    val projectEsService: EventSourcingService<UUID, ProjectAggregate, ProjectAggregateState>

    ) {
    @PostMapping("/{taskName}")
    fun createTask(@PathVariable taskName: String, @RequestParam projectId: UUID, @RequestParam executors: List<UUID>) : TaskCreatedEvent {
        if (projectEsService.getState(projectId) == null) {
            throw IllegalArgumentException("No project with id $projectId")
        }
        return taskEsService.create { it.create(UUID.randomUUID(), taskName, projectId, executors) }
    }

    @PostMapping("/{taskId}")
    fun renameTask(@PathVariable taskId: UUID, @RequestParam taskName: String) : TaskRenamedEvent {
        if (taskEsService.getState(taskId) == null) {
            throw IllegalArgumentException("No task with id $taskId")
        }
        return taskEsService.update(taskId) { it.rename(taskId, taskName) }
    }

    @PostMapping("/{taskId}")
    fun setExecutors(@PathVariable taskId: UUID, @RequestParam executors: List<UUID>) : ExecutorsChangedEvent {
        if (taskEsService.getState(taskId) == null) {
            throw IllegalArgumentException("No task with id $taskId")
        }
        return taskEsService.update(taskId) { it.setExecutors(taskId, executors) }
    }



    // Этого тут быть не должно
    // не ивент сорсинг
    @GetMapping("/{taskId}")
    fun getTask(@PathVariable taskId: UUID) : TaskAggregateState? {
        return taskEsService.getState(taskId)
    }

}
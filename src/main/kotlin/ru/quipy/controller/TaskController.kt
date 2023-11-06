package ru.quipy.controller

import org.springframework.web.bind.annotation.*
import ru.quipy.api.*
import ru.quipy.config.Services
import ru.quipy.logic.*
import java.util.*

@RestController
@RequestMapping("/tasks")
class TaskController(
    val services: Services
    ) {
    @PostMapping("/{taskName}")
    fun createTask(@PathVariable taskName: String, @RequestParam projectId: UUID, @RequestParam executors: List<UUID>) : TaskCreatedEvent {
        return services.taskEsService.create { it.create(UUID.randomUUID(), taskName, projectId, executors, services) }
    }

    @PostMapping("/{taskId}/rename")
    fun renameTask(@PathVariable taskId: UUID, @RequestParam taskName: String) : TaskRenamedEvent {
        return services.taskEsService.update(taskId) { it.rename(taskId, taskName, services) }
    }

    @PostMapping("/{taskId}/setExecutors")
    fun setExecutors(@PathVariable taskId: UUID, @RequestParam executors: List<UUID>) : ExecutorsChangedEvent {
        return services.taskEsService.update(taskId) { it.setExecutors(taskId, executors, services) }
    }



    // Этого тут быть не должно
    // не ивент сорсинг
    @GetMapping("/{taskId}")
    fun getTask(@PathVariable taskId: UUID) : TaskAggregateState? {
        return services.taskEsService.getState(taskId)
    }

}
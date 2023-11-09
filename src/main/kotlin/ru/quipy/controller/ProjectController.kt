package ru.quipy.controller

import org.springframework.web.bind.annotation.*
import ru.quipy.api.MemberAddedEvent
import ru.quipy.api.ProjectCreatedEvent
import ru.quipy.config.Services
import ru.quipy.logic.ProjectAggregateState
import ru.quipy.logic.addMember
import ru.quipy.logic.create
import java.util.*

@RestController
@RequestMapping("/projects")
class ProjectController(
    val services: Services
) {

    @PostMapping("/{projectTitle}")
    fun createProject(@PathVariable projectTitle: String, @RequestParam creatorId: UUID) : ProjectCreatedEvent {
        return services.projectEsService.create { it.create(UUID.randomUUID(), projectTitle, creatorId, services) }
    }

    @PostMapping("/{projectId}/addMember")
    fun addMember(@PathVariable projectId: UUID, @RequestParam userId: UUID) : MemberAddedEvent {
        return services.projectEsService.update(projectId) {it.addMember(projectId, userId, services)}
    }

    @GetMapping("/{projectId}")
    fun getAccount(@PathVariable projectId: UUID) : ProjectAggregateState? {
        return services.projectEsService.getState(projectId)
    }

    @GetMapping("/projects")
    fun getProjects(): List<UUID> {

        return services.projectProjection.projectIds
    }

}

package ru.quipy.controller

import org.springframework.web.bind.annotation.*
import ru.quipy.api.*
import ru.quipy.config.Services
import ru.quipy.logic.*
import java.util.*

@RestController
@RequestMapping("/members")
class MembersController(
    val services: Services
) {
    @PostMapping("")
    fun createMembers(@RequestParam projectId: UUID) : MembersCreatedEvent {
        if (services.taskEsService.getState(UUID.randomUUID()) == null)
            println("HEHE CONTR")
        return services.membersEsService.create { it.create(UUID.randomUUID(), projectId, services) }
    }

    @PostMapping("/{membersId}/addMember")
    fun addMember(@PathVariable membersId: UUID, @RequestParam userId: UUID) : MemberAddedEvent {
        return services.membersEsService.update(membersId) { it.addMember(membersId, userId, services) }
    }

}
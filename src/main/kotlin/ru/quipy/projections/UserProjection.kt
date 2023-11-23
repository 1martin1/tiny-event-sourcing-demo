package ru.quipy.projections

import org.springframework.stereotype.Service
import java.util.*

@Service
class UserProjection {
    var userIds: MutableList<UUID> = mutableListOf()
    var userProjects: MutableMap<UUID, MutableList<UUID>> = mutableMapOf()
    var userLogins: MutableMap<UUID, String> = mutableMapOf()

    fun addUser(id: UUID, login: String) {
        userIds.add(id)
        userLogins[id] = login
    }
    fun addProjectToUser(userId: UUID, projectId: UUID) {
        if (userProjects[userId] == null) {
            userProjects[userId] = mutableListOf(projectId)
        } else {
            userProjects[userId]!!.add(projectId)
        }
    }
}

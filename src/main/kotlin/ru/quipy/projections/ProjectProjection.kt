package ru.quipy.projections

import org.springframework.stereotype.Service
import java.util.*

@Service
class ProjectProjection {
    var projectIds: MutableList<UUID> = mutableListOf()
    var projectMembers: MutableMap<UUID, MutableList<UUID>> = mutableMapOf()
    var projectTasks: MutableMap<UUID, MutableList<UUID>> = mutableMapOf()

    fun addProject(id: UUID) {
        projectIds.add(id)
    }

    fun addMemberToProject(projectId: UUID, userId: UUID) {
        if (projectMembers[projectId] == null) {
            projectMembers[projectId] = mutableListOf(userId)
        } else {
            projectMembers[projectId]!!.add(userId)
        }
    }

    fun addTaskToProject(projectId: UUID, taskId: UUID) {
        if (projectTasks[projectId] == null) {
            projectTasks[projectId] = mutableListOf(taskId)
        } else {
            projectTasks[projectId]!!.add(taskId)
        }
    }
}

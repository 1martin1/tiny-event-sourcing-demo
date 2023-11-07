package ru.quipy.projections

import org.springframework.stereotype.Service
import java.util.*

@Service
class ProjectProjection {
    var projectIds: MutableList<UUID> = mutableListOf()

    fun addProject(id: UUID) {
        projectIds.add(id)
    }
}

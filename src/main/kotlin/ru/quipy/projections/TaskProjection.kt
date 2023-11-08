package ru.quipy.projections

import org.springframework.stereotype.Service
import java.util.*

@Service
class TaskProjection {
    var taskIds: MutableList<UUID> = mutableListOf()

    fun addTask(id: UUID) {
        taskIds.add(id)
    }
}

package ru.quipy.projections

import org.springframework.stereotype.Service
import java.util.*

@Service
class UserProjection {
    var userIds: MutableList<UUID> = mutableListOf()

    fun addUser(id: UUID) {
        userIds.add(id)
    }
}

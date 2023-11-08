package ru.quipy.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.quipy.api.ProjectAggregate
import ru.quipy.api.StatusAggregate
import ru.quipy.api.TaskAggregate
import ru.quipy.api.UserAggregate
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.ProjectAggregateState
import ru.quipy.logic.StatusAggregateState
import ru.quipy.logic.TaskAggregateState
import ru.quipy.logic.UserAggregateState
import ru.quipy.projections.ProjectProjection
import ru.quipy.projections.TaskProjection
import ru.quipy.projections.UserProjection
import java.util.*

@Service
class Services @Autowired constructor (
    val taskEsService: EventSourcingService<UUID, TaskAggregate, TaskAggregateState>,
    val projectEsService: EventSourcingService<UUID, ProjectAggregate, ProjectAggregateState>,
    val userEsService: EventSourcingService<UUID, UserAggregate, UserAggregateState>,
    val statusEsService: EventSourcingService<UUID, StatusAggregate, StatusAggregateState>,
    val projectProjection: ProjectProjection,
    val taskProjection: TaskProjection,
    val userProjection: UserProjection,
)
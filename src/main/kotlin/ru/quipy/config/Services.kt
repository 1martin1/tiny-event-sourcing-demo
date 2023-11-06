package ru.quipy.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.quipy.api.*
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.*
import java.util.*

@Service
class Services @Autowired constructor (
    val taskEsService: EventSourcingService<UUID, TaskAggregate, TaskAggregateState>,
    val projectEsService: EventSourcingService<UUID, ProjectAggregate, ProjectAggregateState>,
    val userEsService: EventSourcingService<UUID, UserAggregate, UserAggregateState>,
    val statusEsService: EventSourcingService<UUID, StatusAggregate, StatusAggregateState>,
)
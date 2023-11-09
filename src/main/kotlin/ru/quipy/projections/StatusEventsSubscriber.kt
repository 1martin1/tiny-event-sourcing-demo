package ru.quipy.projections

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Service
import ru.quipy.api.StatusAggregate
import ru.quipy.api.StatusCreatedEvent
import ru.quipy.config.Services
import ru.quipy.logic.addStatus
import ru.quipy.streams.AggregateSubscriptionsManager
import javax.annotation.PostConstruct

@Service
class StatusEventsSubscriber {

    val logger: Logger = LoggerFactory.getLogger(StatusEventsSubscriber::class.java)

    @Autowired
    lateinit var subscriptionsManager: AggregateSubscriptionsManager
    //    @Autowired
//    lateinit var taskProjection: TaskProjection
    @Autowired
    lateinit var services: Services
    @Autowired
    lateinit var mongoTemplate: MongoTemplate

    @PostConstruct
    fun init() {
        mongoTemplate.updateFirst(Query.query(Criteria.where("_id").`is`("status-event-listener")), Update.update("readIndex", 0) ,"event-stream-read-index")
        mongoTemplate.updateFirst(Query.query(Criteria.where("_id").`is`("status-event-listener")), Update.update("version", 0) ,"event-stream-read-index")
        subscriptionsManager.createSubscriber(StatusAggregate::class, "status-event-listener") {

            `when`(StatusCreatedEvent::class) { event ->

                logger.info("Status created: {}", event.statusName)
                services.taskProjection.addTask(event.projectId)

                services.projectEsService.update(event.projectId) {it.addStatus(event, services)}
            }
        }
    }
}
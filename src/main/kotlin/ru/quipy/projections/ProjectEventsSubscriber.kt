package ru.quipy.projections

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Service
import ru.quipy.api.ProjectAggregate
import ru.quipy.api.ProjectCreatedEvent
import ru.quipy.streams.AggregateSubscriptionsManager
import javax.annotation.PostConstruct

@Service
class ProjectEventsSubscriber {

    val logger: Logger = LoggerFactory.getLogger(ProjectEventsSubscriber::class.java)

    @Autowired
    lateinit var subscriptionsManager: AggregateSubscriptionsManager
    @Autowired
    lateinit var projectProjection: ProjectProjection
    @Autowired
    lateinit var mongoTemplate: MongoTemplate

    @PostConstruct
    fun init() {
        mongoTemplate.updateFirst(Query.query(Criteria.where("_id").`is`("project-event-listener")), Update.update("readIndex", 0) ,"event-stream-read-index")
        mongoTemplate.updateFirst(Query.query(Criteria.where("_id").`is`("project-event-listener")), Update.update("version", 0) ,"event-stream-read-index")
        subscriptionsManager.createSubscriber(ProjectAggregate::class, "project-event-listener") {

            `when`(ProjectCreatedEvent::class) { event ->

                println("((")
                logger.info("Project created: {}", event.title)
                projectProjection.addProject(event.projectId)
            }
        }
    }
}
package ru.quipy.projections

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Service
import ru.quipy.api.MemberAddedEvent
import ru.quipy.api.ProjectAggregate
import ru.quipy.api.ProjectCreatedEvent
import ru.quipy.api.TaskAddedEvent
import ru.quipy.config.Services
import ru.quipy.streams.AggregateSubscriptionsManager
import javax.annotation.PostConstruct

@Service
class ProjectEventsSubscriber {

    val logger: Logger = LoggerFactory.getLogger(ProjectEventsSubscriber::class.java)

    @Autowired
    lateinit var subscriptionsManager: AggregateSubscriptionsManager
    @Autowired
    lateinit var services: Services

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
                logger.info("Project created: {}", event.title)
                projectProjection.addProject(event.projectId)
                projectProjection.addMemberToProject(event.projectId, event.creatorId);
                services.userProjection.addProjectToUser(event.creatorId, event.projectId)
            }

            `when`(MemberAddedEvent::class) { event ->
                projectProjection.addMemberToProject(event.projectId, event.userId);
                services.userProjection.addProjectToUser(event.userId, event.projectId)
            }

            `when`(TaskAddedEvent::class) { event ->
                projectProjection.addTaskToProject(event.taskCreatedEvent.projectId, event.taskCreatedEvent.taskId);
            }
        }
    }
}
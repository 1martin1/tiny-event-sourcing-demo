package ru.quipy.projections

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.quipy.api.TaskAggregate
import ru.quipy.api.TaskCreatedEvent
import ru.quipy.streams.AggregateSubscriptionsManager
import javax.annotation.PostConstruct

@Service
class TasksEventsSubscriber {

    val logger: Logger = LoggerFactory.getLogger(TasksEventsSubscriber::class.java)

    @Autowired
    lateinit var subscriptionsManager: AggregateSubscriptionsManager
    @Autowired
    lateinit var taskProjection: TaskProjection

    @PostConstruct
    fun init() {
        subscriptionsManager.createSubscriber(TaskAggregate::class, "task-event-listener") {

            `when`(TaskCreatedEvent::class) { event ->

                logger.info("Task created: {}", event.taskName)
                taskProjection.addTask(event.projectId)
            }
        }
    }
}
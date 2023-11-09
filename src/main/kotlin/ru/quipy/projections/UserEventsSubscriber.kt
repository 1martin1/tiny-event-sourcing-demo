package ru.quipy.projections

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Service
import ru.quipy.api.UserAggregate
import ru.quipy.api.UserCreatedEvent
import ru.quipy.streams.AggregateSubscriptionsManager
import javax.annotation.PostConstruct

@Service
class UserEventsSubscriber {

    val logger: Logger = LoggerFactory.getLogger(UserEventsSubscriber::class.java)

    @Autowired
    lateinit var subscriptionsManager: AggregateSubscriptionsManager
    @Autowired
    lateinit var userProjection: UserProjection
    @Autowired
    lateinit var mongoTemplate: MongoTemplate

    @PostConstruct
    fun init() {
        mongoTemplate.updateFirst(Query.query(Criteria.where("_id").`is`("user-event-listener")), Update.update("readIndex", 0) ,"event-stream-read-index")
        mongoTemplate.updateFirst(Query.query(Criteria.where("_id").`is`("user-event-listener")), Update.update("version", 0) ,"event-stream-read-index")

        subscriptionsManager.createSubscriber(UserAggregate::class, "user-event-listener") {

            `when`(UserCreatedEvent::class) { event ->

                logger.info("User created: {}", event.nickname)
                userProjection.addUser(event.userId)
            }
        }
    }
}
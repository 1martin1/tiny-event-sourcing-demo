package ru.quipy.projections

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
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

    @PostConstruct
    fun init() {
        subscriptionsManager.createSubscriber(UserAggregate::class, "user-event-listener") {

            `when`(UserCreatedEvent::class) { event ->

                logger.info("User created: {}", event.nickname)
                userProjection.addUser(event.userId)
            }
        }
    }
}
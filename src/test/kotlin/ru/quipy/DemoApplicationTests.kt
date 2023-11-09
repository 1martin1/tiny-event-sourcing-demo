package ru.quipy

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.runApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.test.context.event.annotation.BeforeTestClass
import ru.quipy.api.ProjectAggregate
import ru.quipy.api.StatusAggregate
import ru.quipy.api.TaskAggregate
import ru.quipy.api.UserAggregate
import ru.quipy.config.Services
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.*
import java.util.*

@SpringBootTest
class DemoApplicationTests {
	companion object{
		private val user2Id = UUID.randomUUID()
		private val userId = UUID.randomUUID()
		private val projectId = UUID.randomUUID()
		private val taskId = UUID.randomUUID()
		private val testId = UUID.randomUUID()
		private val testNickname = "Martin"
		private val testName = "Matvey"
		private val testPassword = "qwerty"
		private val testProjectTitle = "MyProject"
		private val testTaskName = "MyTask"
		private val testNewTaskName = "MyNewTask"
//		private val testStatusName = "In_Progress"
		private val testNewStatusName = "Done"
		private val testStatusName = "Done"

		@JvmStatic
		@BeforeTestClass
		fun start(): Unit {
			runApplication<DemoApplication>()
		}
	}

	@Autowired
	private lateinit var userEsService: EventSourcingService<UUID, UserAggregate, UserAggregateState>

	@Autowired
	private lateinit var projectEsService: EventSourcingService<UUID, ProjectAggregate, ProjectAggregateState>

	@Autowired
	private lateinit var taskEsService: EventSourcingService<UUID, TaskAggregate, TaskAggregateState>

	@Autowired
	private lateinit var statusEsService: EventSourcingService<UUID, StatusAggregate, StatusAggregateState>

	@Autowired
	private lateinit var services: Services

	@Autowired
	private lateinit var mongoTemplate: MongoTemplate

	@BeforeEach
	fun init(){
		cleanDatabase()
	}

	fun cleanDatabase(){
		mongoTemplate.remove(Query.query(Criteria.where("aggregateId").`is`(testId)), "aggregate-user")
		mongoTemplate.remove(Query.query(Criteria.where("aggregateId").`is`(userId)), "aggregate-user")
		mongoTemplate.remove(Query.query(Criteria.where("aggregateId").`is`(testId)), "aggregate-project")
		mongoTemplate.remove(Query.query(Criteria.where("aggregateId").`is`(projectId)), "aggregate-project")
		mongoTemplate.remove(Query.query(Criteria.where("aggregateId").`is`(testId)), "aggregate-task")
		mongoTemplate.remove(Query.query(Criteria.where("aggregateId").`is`(taskId)), "aggregate-task")
		mongoTemplate.remove(Query.query(Criteria.where("aggregateId").`is`(testId)), "aggregate-status")
		mongoTemplate.remove(Query.query(Criteria.where("_id").`is`(testId)), "snapshots")
	}

	@Test
	fun createUser() {
		userEsService.create {
			it.create(testId, testNickname, testName, testPassword, services)
		}

		val state = userEsService.getState(testId)

		Assertions.assertNotEquals(state, null)

		if (state != null) {
			Assertions.assertEquals(testId, state.getId())
			Assertions.assertEquals(testNickname, state.nickname)
			Assertions.assertEquals(testName, state.name)
			Assertions.assertEquals(testPassword, state.password)
		}
	}

	@Test
	fun createUsersWithEqualIDs() {
		userEsService.create {
			it.create(testId, testNickname, testName, testPassword, services)
		}


		Assertions.assertThrows(Throwable::class.java){
			userEsService.create {
				it.create(testId, testNickname, testName, testPassword, services)
			}
		}
	}

	@Test
	fun createProject(){
		userEsService.create {
			it.create(userId, testNickname, testName, testPassword, services)
		}

		projectEsService.create {
			it.create(testId, testProjectTitle, userId, services)
		}

		val state = projectEsService.getState(testId)

		Assertions.assertNotEquals(state, null)

		if (state != null) {
			Assertions.assertEquals(testId, state.getId())
			Assertions.assertEquals(testProjectTitle, state.title)
			Assertions.assertEquals(userId, state.creatorId)
			Assertions.assertEquals(0, state.tasks.size)
			Assertions.assertEquals(userId, state.members.firstOrNull())
			Assertions.assertEquals(0, state.statuses.size)
		}
	}

	@Test
	fun addMemberToProject(){
		userEsService.create {
			it.create(userId, testNickname, testName, testPassword, services)
		}

		projectEsService.create {
			it.create(testId, testProjectTitle, userId, services)
		}

		projectEsService.update(testId){
			it.addMember(testId, user2Id, services)
		}

		val state = projectEsService.getState(testId)

		Assertions.assertNotEquals(state, null)

		if (state != null) {
			Assertions.assertEquals(listOf(userId, user2Id), state.members)
		}
	}

	@Test
	fun addAlreadyAddedMemberToProject(){
		userEsService.create {
			it.create(userId, testNickname, testName, testPassword, services)
		}

		projectEsService.create {
			it.create(testId, testProjectTitle, userId, services)
		}

		Assertions.assertThrows(Throwable::class.java){
			projectEsService.update(testId){
				it.addMember(testId, userId, services)
			}
		}
	}

	@Test
	fun createTask(){
		userEsService.create {
			it.create(userId, testNickname, testName, testPassword, services)
		}

		projectEsService.create {
			it.create(projectId, testProjectTitle, userId, services)
		}

		taskEsService.create {
			it.create(testId, testTaskName, projectId, listOf(), services)
		}

		val state = taskEsService.getState(testId)

		Assertions.assertNotEquals(state, null)

		if (state != null) {
			Assertions.assertEquals(testId, state.getId())
			Assertions.assertEquals(testTaskName, state.taskName)
			Assertions.assertEquals(projectId, state.projectId)
			Assertions.assertEquals(0, state.executors.size)
		}
		val projectState = projectEsService.getState(projectId)

		Assertions.assertNotEquals(projectState, null)

		if (projectState != null) {
			Assertions.assertEquals(testId, projectState.tasks.firstOrNull())
		}
	}

	@Test
	fun renameTask(){
		userEsService.create {
			it.create(userId, testNickname, testName, testPassword, services)
		}

		projectEsService.create {
			it.create(projectId, testProjectTitle, userId, services)
		}

		taskEsService.create {
			it.create(testId, testTaskName, projectId, listOf(), services)
		}

		var state = taskEsService.getState(testId)

		Assertions.assertNotEquals(state, null)

		if (state != null) {
			Assertions.assertEquals(testTaskName, state.taskName)
		}

		taskEsService.update(testId){
			it.rename(testId, testNewTaskName, services)
		}

		state = taskEsService.getState(testId)

		Assertions.assertNotEquals(state, null)

		if (state != null) {
			Assertions.assertEquals(testNewTaskName, state.taskName)
		}
	}

	@Test
	fun addExecutorToTask(){
		userEsService.create {
			it.create(userId, testNickname, testName, testPassword, services)
		}

		projectEsService.create {
			it.create(projectId, testProjectTitle, userId, services)
		}

		taskEsService.create {
			it.create(testId, testTaskName, projectId, listOf(), services)
		}

		taskEsService.update(testId){
			it.setExecutors(testId, listOf(userId, user2Id), services)
		}

		val state = taskEsService.getState(testId)

		Assertions.assertNotEquals(state, null)

		if (state != null) {
			Assertions.assertEquals(listOf(userId, user2Id), state.executors)
		}
	}

	@Test
	fun createStatus(){
		userEsService.create {
			it.create(userId, testNickname, testName, testPassword, services)
		}

		projectEsService.create {
			it.create(projectId, testProjectTitle, userId, services)
		}

		taskEsService.create {
			it.create(taskId, testTaskName, projectId, listOf(), services)
		}

		statusEsService.create {
			it.create(testId, testStatusName, projectId, taskId, services)
		}

		val state = statusEsService.getState(testId)

		Assertions.assertNotEquals(state, null)

		if (state != null) {
			Assertions.assertEquals(testId, state.getId())
			Assertions.assertEquals(testStatusName, state.name)
		}

		val projectState = projectEsService.getState(projectId)

		Assertions.assertNotEquals(projectState, null)

		if (projectState != null) {
			Assertions.assertEquals(testId, projectState.statuses.firstOrNull())
		}
	}

	@Test
	fun updateStatus(){
		userEsService.create {
			it.create(userId, testNickname, testName, testPassword, services)
		}

		projectEsService.create {
			it.create(projectId, testProjectTitle, userId, services)
		}

		taskEsService.create {
			it.create(taskId, testTaskName, projectId, listOf(), services)
		}

		statusEsService.create {
			it.create(testId, testStatusName, projectId, taskId, services)
		}

		statusEsService.update(testId){
			it.update(testId, testNewStatusName, services)
		}

		val state = statusEsService.getState(testId)

		Assertions.assertNotEquals(state, null)

		if (state != null) {
			Assertions.assertEquals(testNewStatusName, state.name)
		}
	}

}

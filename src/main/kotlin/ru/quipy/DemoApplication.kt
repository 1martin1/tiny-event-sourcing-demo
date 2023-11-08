package ru.quipy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DemoApplication


fun main(args: Array<String>) {


//	val mc = MongoClients.create("mongodb://localhost:27017")
//	mc.listDatabaseNames().forEach{println(it)}
	runApplication<DemoApplication>(*args)
}

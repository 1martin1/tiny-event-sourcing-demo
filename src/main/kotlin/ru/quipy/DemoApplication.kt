package ru.quipy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.quipy.config.Services

@SpringBootApplication
class DemoApplication(val services: Services)

fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}

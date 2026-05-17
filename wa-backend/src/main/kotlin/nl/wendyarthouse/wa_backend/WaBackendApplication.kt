package nl.wendyarthouse.wa_backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WaBackendApplication

fun main(args: Array<String>) {
	runApplication<WaBackendApplication>(*args)
}

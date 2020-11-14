package pl.lodz.nosql.victims

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class VictimsApplication

fun main(args: Array<String>) {
    runApplication<VictimsApplication>(*args)
}

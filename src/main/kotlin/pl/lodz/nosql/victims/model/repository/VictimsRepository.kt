package pl.lodz.nosql.victims.model.repository

import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.repository.MongoRepository
import pl.lodz.nosql.victims.model.data.Victim
import java.util.*

interface VictimsRepository : MongoRepository<Victim, String> {

    fun findByNameContains(name: String, sort: Sort): List<Victim>

    fun findByAge(age: Int, sort: Sort): List<Victim>

    fun findByGender(gender: String, sort: Sort): List<Victim>

    fun findByRace(race: String, sort: Sort): List<Victim>

    fun findByDate(date: Date, sort: Sort): List<Victim>

    fun findByCity(city: String, sort: Sort): List<Victim>

    fun findByState(state: String, sort: Sort): List<Victim>

    fun findByMannerOfDeathContains(mannerOfDeath: String, sort: Sort): List<Victim>

    fun findByArmedContains(armed: String, sort: Sort): List<Victim>

    fun findByMentalIllness(mentalIllness: Boolean, sort: Sort): List<Victim>

    fun findByFlee(flee: Boolean, sort: Sort): List<Victim>

    fun findByDateBetweenOrderByDateDesc(from: Date, to: Date): List<Victim>

    fun findByDateAfterOrderByDateDesc(from: Date): List<Victim>

    fun findByDateBeforeOrderByDateDesc(to: Date): List<Victim>
}
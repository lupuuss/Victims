package pl.lodz.nosql.victims.model.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import pl.lodz.nosql.victims.model.data.Victim
import pl.lodz.nosql.victims.model.data.VictimDTO
import pl.lodz.nosql.victims.model.data.VictimDetails
import pl.lodz.nosql.victims.model.repository.IdGenerator
import pl.lodz.nosql.victims.model.repository.VictimsRepository
import java.util.*
import java.util.stream.Stream
import kotlin.streams.toList

@Service
class VictimsService {

    @Autowired
    private lateinit var repository: VictimsRepository

    @Autowired
    private lateinit var idGenerator: IdGenerator

    fun getVictims(offset: Long, limit: Long, sortBy: String?, sortDesc: Boolean): List<Victim> {

        val sort = if (sortBy != null) {

            val direction = if (sortDesc) Sort.Direction.DESC else Sort.Direction.ASC
            Sort.by(direction, sortBy)

        } else {
            Sort.unsorted()
        }

        return repository.findAll(sort)
                .stream()
                .skip(offset)
                .limit(limit)
                .toList()
    }

    fun getVictims(
            offset: Long, limit: Long,
            sortBy: String?, sortDesc: Boolean,
            property: Victim.Property<*>, value: String
    ): List<Victim> {

        val direction = if (sortDesc) {
            Sort.Direction.DESC
        } else {
            Sort.Direction.ASC
        }

        val sort = if (sortBy != null) {
            Sort.by(direction, sortBy)
        } else {
            Sort.by(direction, property.name)
        }

        val stream: Stream<Victim> = findBy(property, value, sort)

        return stream
                .skip(offset)
                .limit(limit)
                .toList()
    }

    private fun findBy(property: Victim.Property<*>, value: String, sort: Sort): Stream<Victim> {

        return when (property) {
            is Victim.Property.Id -> repository.findById(value).map { listOf(it) }.orElseGet { emptyList() }
            is Victim.Property.Name -> repository.findByNameContains(value, sort)
            is Victim.Property.Age -> repository.findByAge(property.mapArg(value), sort)
            is Victim.Property.Gender -> repository.findByGender(value, sort)
            is Victim.Property.Race -> repository.findByRace(value, sort)
            is Victim.Property.Date -> repository.findByDate(property.mapArg(value), sort)
            is Victim.Property.City -> repository.findByCity(value, sort)
            is Victim.Property.State -> repository.findByState(value, sort)
            is Victim.Property.MannerOfDeath -> repository.findByMannerOfDeathContains(value, sort)
            is Victim.Property.Armed -> repository.findByArmedContains(value, sort)
            is Victim.Property.MentalIllness -> repository.findByMentalIllness(property.mapArg(value), sort)
            is Victim.Property.Flee -> repository.findByFlee(property.mapArg(value), sort)
            else -> throw PropertyNotSupportedException(property)
        }.stream()
    }

    fun getVictimsByDateBoundaries(from: Date?, to: Date?): List<Victim> {

        return when {
            from != null && to != null -> repository.findByDateBetweenOrderByDateDesc(from, to)
            from != null -> repository.findByDateAfterOrderByDateDesc(from)
            to != null -> repository.findByDateBeforeOrderByDateDesc(to)
            else -> repository.findAll(Sort.by(Sort.Direction.DESC, "date"))
        }
    }


    fun addNewVictim(victimDetails: VictimDetails): Victim {

        val victim = victimDetails.toVictim(idGenerator.generate(), Date())

        return repository.save(victim)
    }

    fun removeVictim(id: String) {

        val victim = repository.findById(id).orElseThrow { throw VictimNotFoundException(id) }

        repository.deleteById(victim.id)
    }

    fun patchVictimWithId(id: String, dto: VictimDTO): Victim {

        val victim = repository.findById(id).orElseThrow { throw VictimNotFoundException(id) }

        println(dto)

        val newVictim = Victim(
                id,
                processProperty(dto, Victim.Property.Name, victim.name),
                processProperty(dto, Victim.Property.Age, victim.age),
                processProperty(dto, Victim.Property.Gender, victim.gender),
                processProperty(dto, Victim.Property.Race, victim.race),
                victim.date,
                processProperty(dto, Victim.Property.City, victim.city),
                processProperty(dto, Victim.Property.State, victim.state),
                processProperty(dto, Victim.Property.MannerOfDeath, victim.mannerOfDeath),
                processProperty(dto, Victim.Property.Armed, victim.armed),
                processProperty(dto, Victim.Property.MentalIllness, victim.mentalIllness),
                processProperty(dto, Victim.Property.Flee, victim.flee
                )
        )

        return repository.save(newVictim)
    }

    private fun <T> processProperty(dto: VictimDTO, property: Victim.Property<T>, currentValue: T?): T? {
        val passedValue = dto.accessValue(property) ?: return currentValue

        return if (passedValue.isPresent) {
            passedValue.get()
        } else {
            null
        }

    }

    fun getVictims(id: String): Victim {
        return repository.findById(id).orElseThrow { throw VictimNotFoundException(id) }
    }
}
package pl.lodz.nosql.victims.model.data

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.format.annotation.DateTimeFormat
import pl.lodz.nosql.victims.model.services.PropertyArgumentConversionException
import pl.lodz.nosql.victims.model.services.PropertyInvalidArgumentException
import pl.lodz.nosql.victims.model.services.PropertyNotFoundException
import java.text.SimpleDateFormat
import java.util.*

@Document(collection = "PoliceVictims")
data class Victim (
        @Id
        val id : String,

        @Field("Name")
        val name : String?,

        @Field("Age")
        val age : Int?,

        @Field("Gender")
        val gender : String?,

        @Field("Race")
        val race : String?,

        @Field("Date")
        @DateTimeFormat(pattern =dateStringFormat)
        val date : Date,

        @Field("City")
        val city : String?,

        @Field("State")
        val state : String?,

        @Field("Manner_of_death")
        val mannerOfDeath : String?,

        @Field("Armed")
        val armed : String?,

        @Field("Mental_illness")
        val mentalIllness : Boolean?,

        @Field("Flee")
        val flee : Boolean?
) {
    sealed class Property<T> {

        val name: String = javaClass.simpleName

        abstract fun mapArg(value: String): T

        abstract class StringBased : Property<String?>() {
            override fun mapArg(value: String): String = value
        }

        abstract class BooleanBased : Property<Boolean?>() {
            override fun mapArg(value: String): Boolean {
                return try {
                    value.toBoolean()
                } catch (e: Exception) {
                    throw PropertyArgumentConversionException(value, "Boolean", e)
                }
            }
        }

        abstract class IntBased : Property<Int?>() {
            override fun mapArg(value: String): Int {
                return try {
                    value.toInt()
                } catch (e: NumberFormatException) {
                    throw PropertyArgumentConversionException(value, "Int", e)
                }
            }
        }

        abstract class DateBased : Property<java.util.Date>() {
            override fun mapArg(value: String): java.util.Date {
                return try {
                    SimpleDateFormat(dateStringFormat).parse(value)
                } catch (e: java.text.ParseException) {
                    throw PropertyArgumentConversionException(value, "Date(format='$dateStringFormat')", e)
                }
            }
        }

        object Id : StringBased()
        object Name : StringBased()
        object Age : IntBased() {
            override fun mapArg(value: String): Int {
                val parsedValue = super.mapArg(value)

                if (parsedValue <= 0) {
                    throw PropertyInvalidArgumentException("Age must be a positive number! Passed: $parsedValue")
                } else {
                    return parsedValue
                }
            }
        }
        object Gender : StringBased()
        object Race : StringBased()
        object Date : DateBased()
        object City : StringBased()
        object State : StringBased()
        object MannerOfDeath : StringBased()
        object Armed : StringBased()
        object MentalIllness : BooleanBased()
        object Flee : BooleanBased()

        companion object {
            val values: List<Property<*>> = listOf(
                    Id, Name, Age,
                    Gender, Race, Date,
                    City, State, MannerOfDeath,
                    Armed, MentalIllness, Flee
            )

            fun of(name: String) = values.find { it.name.equals(name, ignoreCase = true) }
                    ?: throw PropertyNotFoundException(name)
        }
    }

    companion object {
        const val dateStringFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
    }
}

data class VictimDetails(
        val name : String?,
        val age : Int?,
        val gender : String?,
        val race : String?,
        val city : String?,
        val state : String?,
        val mannerOfDeath : String?,
        val armed : String?,
        val mentalIllness : Boolean?,
        val flee : Boolean?
) {
    fun toVictim(id: String, date: Date) = Victim(
            id, name, age, gender, race, date, city, state,
            mannerOfDeath, armed, mentalIllness, flee
    )
}

class VictimDTO {
    private val values: MutableMap<Victim.Property<*>, String?> = mutableMapOf()

    @Field("name")
    fun setName(name: String?) {
        values[Victim.Property.Name] = name
    }

    @Field("age")
    fun setAge(age: Int?) {
        values[Victim.Property.Age] = age?.toString()
    }

    @Field("gender")
    fun setGender(gender: String?) {
        values[Victim.Property.Gender] = gender
    }

    @Field("race")
    fun setRace(race: String?) {
        values[Victim.Property.Race] = race
    }

    @Field("date")
    fun setDate(date: Date?) {
        values[Victim.Property.Date] = date?.toString()
    }
    @Field("city")
    fun setCity(city: String?) {
        values[Victim.Property.City] = city
    }

    @Field("state")
    fun setState(state: String?) {
        values[Victim.Property.State] = state
    }

    @Field("mannerOfDeath")
    fun setMannerOfDeath(mannerOfDeath: String?) {
        values[Victim.Property.MannerOfDeath] = mannerOfDeath
    }

    @Field("armed")
    fun setArmed(armed: String?) {
        values[Victim.Property.Armed] = armed
    }

    @Field("mentalIllness")
    fun setMentalIllness(mentalIllness: Boolean?) {
        values[Victim.Property.MentalIllness] = mentalIllness?.toString()
    }

    @Field("Flee")
    fun setFlee(flee: Boolean?) {
        values[Victim.Property.Flee] = flee?.toString()
    }


    fun accessValues() = values

    fun <T> accessValue(property: Victim.Property<T>): Optional<T>? {
        if (!values.containsKey(property)) {
            return null
        }

        return values[property]?.let {
            Optional.of(property.mapArg(it))
        } ?: Optional.empty()
    }

    fun toVictim(id: String): Victim = Victim(
            id,
            accessValue(Victim.Property.Name)?.get(),
            accessValue(Victim.Property.Age)?.get(),
            accessValue(Victim.Property.Gender)?.get(),
            accessValue(Victim.Property.Race)?.get(),
            accessValue(Victim.Property.Date)?.get() ?: Date(),
            accessValue(Victim.Property.City)?.get(),
            accessValue(Victim.Property.State)?.get(),
            accessValue(Victim.Property.MannerOfDeath)?.get(),
            accessValue(Victim.Property.Armed)?.get(),
            accessValue(Victim.Property.MentalIllness)?.get(),
            accessValue(Victim.Property.Flee)?.get()
    )
}
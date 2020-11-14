package pl.lodz.nosql.victims.model.data

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.format.annotation.DateTimeFormat
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

        abstract class StringBased : Property<String>() {
            override fun mapArg(value: String): String = value
        }

        abstract class BooleanBased : Property<Boolean>() {
            override fun mapArg(value: String): Boolean = value.toBoolean()
        }

        abstract class IntBased : Property<Int>() {
            override fun mapArg(value: String): Int = value.toInt()
        }

        abstract class DateBased : Property<java.util.Date>() {
            override fun mapArg(value: String): java.util.Date {
                return SimpleDateFormat(dateStringFormat).parse(value)
            }
        }

        object Id : StringBased()
        object Name : StringBased()
        object Age : IntBased()
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

            fun of(name: String) = values.find { it.name.equals(name, ignoreCase = true) }!!
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
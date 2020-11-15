package pl.lodz.nosql.victims.model.services

import org.springframework.http.HttpStatus
import pl.lodz.nosql.victims.model.data.Victim


open class VictimServiceException(
        val status: HttpStatus,
        message: String? = null,
        cause: Throwable? = null
) : Exception(message, cause)

class VictimNotFoundException(id: String) : VictimServiceException(
        HttpStatus.NOT_FOUND,
        "Victim with id='$id' couldn't be found!"
)

class PropertyInvalidArgumentException(message: String) : VictimServiceException(HttpStatus.BAD_REQUEST, message)

class PropertyArgumentConversionException(passedValue: String, expectedType: String, cause: Throwable): VictimServiceException(
        HttpStatus.BAD_REQUEST,
        "Value '$passedValue' couldn't be converted to $expectedType! Cause: [${cause.javaClass.simpleName}: ${cause.message}]",
        cause
)

class PropertyNotFoundException(passedName: String) : VictimServiceException(
        HttpStatus.BAD_REQUEST,
        "Property '$passedName' not found! Available properties: ${Victim.Property.values.map { it.name }}"
)

class PropertyNotSupportedException(
        property: Victim.Property<*>
) : VictimServiceException(HttpStatus.BAD_REQUEST, "Property '${property.name}' exists, but it's not supported in this query!")
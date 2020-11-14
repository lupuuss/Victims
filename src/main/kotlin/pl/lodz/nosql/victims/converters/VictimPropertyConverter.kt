package pl.lodz.nosql.victims.converters

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import pl.lodz.nosql.victims.model.data.Victim

@Component
class VictimPropertyConverter : Converter<String, Victim.Property<*>> {
    override fun convert(source: String): Victim.Property<*> {
        return Victim.Property.of(source)
    }
}
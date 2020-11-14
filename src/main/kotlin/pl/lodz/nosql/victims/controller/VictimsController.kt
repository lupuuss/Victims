package pl.lodz.nosql.victims.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.*
import pl.lodz.nosql.victims.model.data.Victim
import pl.lodz.nosql.victims.model.data.VictimDetails
import pl.lodz.nosql.victims.model.services.VictimsService
import java.util.*

@RestController
class VictimsController {

    @Autowired
    private lateinit var victimsService: VictimsService

    @GetMapping("/")
    fun getHelp(): String {
        return "TODO help here! :)"
    }

    @GetMapping("/victim")
    fun getVictims(
            @RequestParam(defaultValue = "0") offset: Long,
            @RequestParam(defaultValue = "100") limit: Long,
            @RequestParam sortBy: String?,
            @RequestParam(defaultValue = "false") desc: Boolean
    ): List<Victim> {
        return victimsService.getVictims(offset, limit, sortBy, desc)
    }

    @PostMapping("/victim")
    fun addVictim(@RequestBody details: VictimDetails): Victim  {
        return victimsService.addNewVictim(details)
    }

    @GetMapping("/victim/id/{id}")
    fun getVictimById(@PathVariable id: String): Optional<Victim> {
        return victimsService.getVictims(id)
    }

    @DeleteMapping("/victim/id/{id}")
    fun removeVictimById(@PathVariable id: String) {
        victimsService.removeVictim(id)
    }

    @PutMapping("/victim/id/{id}")
    fun updateVictimWithId(
            @PathVariable id: String,
            @RequestBody victimDetails: VictimDetails
    ): Optional<Victim> {
        return victimsService.updateVictimWithId(id, victimDetails)
    }

    @GetMapping("/victim/by/{property}/{value}")
    fun getVictimsByProperty(
            @RequestParam(defaultValue = "0") offset: Long,
            @RequestParam(defaultValue = "100") limit: Long,
            @RequestParam sortBy: String?,
            @RequestParam(defaultValue = "false") desc: Boolean,
            @PathVariable property: Victim.Property<*>,
            @PathVariable value: String
    ): List<Victim> {

        return victimsService.getVictims(offset, limit, sortBy, desc, property, value)
    }

    @GetMapping("/victim/between/date")
    fun getVictimsBetweenDates(
            @RequestParam @DateTimeFormat(pattern = Victim.dateStringFormat) from: Date?,
            @RequestParam @DateTimeFormat(pattern = Victim.dateStringFormat) to: Date?
    ): List<Victim> {

        return victimsService.getVictimsByDateBoundaries(from, to)
    }
}
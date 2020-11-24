package pl.lodz.nosql.victims.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.*
import pl.lodz.nosql.victims.model.data.Victim
import pl.lodz.nosql.victims.model.data.VictimDTO
import pl.lodz.nosql.victims.model.data.VictimDetails
import pl.lodz.nosql.victims.HelpResponse
import pl.lodz.nosql.victims.model.services.VictimServiceException
import pl.lodz.nosql.victims.model.services.VictimsService
import java.util.*
import javax.servlet.http.HttpServletRequest

@RestController
class VictimsController {

    @Autowired
    private lateinit var victimsService: VictimsService

    @ExceptionHandler(VictimServiceException::class)
    fun handle(
            exception: VictimServiceException,
            request: HttpServletRequest
    ): ErrorMessage {

        return ErrorMessage(
                Date(),
                exception.status.value(),
                exception.status.reasonPhrase,
                exception.message ?: "",
                request.requestURI
        )
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


    @GetMapping("/victim/id/{id}")
    fun getVictimById(@PathVariable id: String): Victim {
        return victimsService.getVictims(id)
    }

    @DeleteMapping("/victim/id/{id}")
    fun removeVictimById(@PathVariable id: String) {
        victimsService.removeVictim(id)
    }

    @PatchMapping("/victim/id/{id}")
    fun updateVictimWithId(
            @PathVariable id: String,
            @RequestBody dto: VictimDTO
    ): Victim {

        return victimsService.updateVictimWithId(id, dto)
    }

    @PutMapping("/victim/id/{id}")
    fun putVictimWithId(@PathVariable id: String, @RequestBody victimDto: VictimDTO): Victim {

        return victimsService.putVictimWithId(id, victimDto.toVictim(id))
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

    @GetMapping("/")
    fun getHelp(): HelpResponse {
        return HelpResponse
    }

}
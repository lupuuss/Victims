package pl.lodz.nosql.victims.controller

import java.util.*

class ErrorMessage(
        val timestamp: Date,
        val status: Int,
        val error: String,
        val message: String,
        val path: String
)

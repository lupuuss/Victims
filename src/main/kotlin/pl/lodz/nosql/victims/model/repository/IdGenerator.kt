package pl.lodz.nosql.victims.model.repository

interface IdGenerator {
    fun generate(): String
}
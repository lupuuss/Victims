package pl.lodz.nosql.victims.model.repository

import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class MongoIdGenerator : IdGenerator {
    override fun generate(): String = ObjectId.get().toString()
}
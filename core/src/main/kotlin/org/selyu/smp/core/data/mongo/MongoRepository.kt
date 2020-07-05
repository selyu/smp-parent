package org.selyu.smp.core.data.mongo

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import org.bson.UuidRepresentation
import org.bson.codecs.DocumentCodec
import org.bson.codecs.UuidCodec
import org.bson.codecs.configuration.CodecRegistries.fromCodecs
import org.selyu.smp.core.data.Repository
import org.selyu.smp.core.data.mongo.store.MongoProfileStore
import org.selyu.smp.core.data.store.ProfileStore
import org.selyu.smp.core.settings.Settings

class MongoRepository : Repository {
    private val mongoClient: MongoClient
    private val mongoDatabase: MongoDatabase

    init {
        val settingsBuilder = MongoClientSettings.builder()
        settingsBuilder.uuidRepresentation(UuidRepresentation.STANDARD)
        settingsBuilder.applyConnectionString(ConnectionString(Settings.CONNECTION_STRING))

        mongoClient = MongoClients.create(settingsBuilder.build())
        mongoDatabase = mongoClient.getDatabase(Settings.DATABASE_NAME)
    }

    override val profileStore: ProfileStore = MongoProfileStore(mongoDatabase.getCollection("profiles"))
    override fun closeConnections() = mongoClient.close()
}
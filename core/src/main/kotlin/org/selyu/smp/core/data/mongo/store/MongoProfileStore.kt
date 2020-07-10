package org.selyu.smp.core.data.mongo.store

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Filters.regex
import com.mongodb.client.model.ReplaceOptions
import org.bson.Document
import org.selyu.smp.core.data.store.ProfileStore
import org.selyu.smp.core.profile.Profile
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletableFuture.runAsync
import java.util.concurrent.CompletableFuture.supplyAsync
import java.util.regex.Pattern

class MongoProfileStore(private val collection: MongoCollection<Document>) : ProfileStore {
    private val replaceOptions = ReplaceOptions().upsert(true)

    override fun getByUsername(username: String): CompletableFuture<Optional<Profile>> = supplyAsync {
        val document = collection.find(regex("username", Pattern.compile(Pattern.quote(username), Pattern.CASE_INSENSITIVE))).first()
        return@supplyAsync Optional.ofNullable(deserialize(document))
    }

    override fun getByKey(key: UUID): CompletableFuture<Optional<Profile>> = supplyAsync {
        val document = collection.find(eq("_id", key)).first()
        return@supplyAsync Optional.ofNullable(deserialize(document))
    }

    override fun save(value: Profile): CompletableFuture<Profile> = supplyAsync {
        val document = Document("_id", value.uniqueId)
        document.append("username", value.username)
        document.append("balance", value.balance)

        collection.replaceOne(eq("_id", value.uniqueId), document, replaceOptions)
        return@supplyAsync value
    }

    override fun delete(value: Profile): CompletableFuture<Void> = runAsync {
        collection.deleteOne(eq("_id", value.uniqueId))
    }

    private fun deserialize(document: Document?): Profile? {
        if (document == null)
            return null

        return Profile(document.get("_id", UUID::class.java), document.getString("username"), document.getDouble("balance"))
    }
}
package org.selyu.smp.core.data

import java.util.*
import java.util.concurrent.CompletableFuture

interface Store<K, V> {
    fun getByKey(key: K): CompletableFuture<Optional<V>>
    fun save(value: V): CompletableFuture<V>
    fun delete(value: V): CompletableFuture<Void>
}
package org.selyu.smp.core.data

import org.selyu.smp.core.data.store.ProfileStore

interface Repository {
    val profileStore: ProfileStore

    /**
     * Called when the plugin disables, Should close all on-going database connections
     */
    fun closeConnections()
}
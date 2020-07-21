package org.selyu.smp.core.data;

import org.jetbrains.annotations.NotNull;
import org.selyu.smp.core.data.store.ProfileStore;

import java.io.Closeable;

public interface Repository extends Closeable {
    @NotNull
    ProfileStore getProfileStore();
}

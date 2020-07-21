package org.selyu.smp.core.data;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface Store<KEY, VALUE> {
    @NotNull
    CompletableFuture<Optional<VALUE>> getByKey(@NotNull KEY key);

    @NotNull
    CompletableFuture<VALUE> save(@NotNull VALUE value);

    @NotNull
    CompletableFuture<Void> delete(@NotNull VALUE value);
}

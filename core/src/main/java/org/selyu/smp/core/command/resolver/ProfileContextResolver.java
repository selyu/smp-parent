package org.selyu.smp.core.command.resolver;

import co.aikar.commands.BukkitCommandExecutionContext;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.contexts.ContextResolver;
import org.selyu.smp.core.Core;
import org.selyu.smp.core.data.Repository;
import org.selyu.smp.core.manager.ProfileManager;
import org.selyu.smp.core.profile.Profile;

import static com.ea.async.Async.await;

public final class ProfileContextResolver implements ContextResolver<Profile, BukkitCommandExecutionContext> {
    private final ProfileManager profileManager = Core.getInstance().getProfileManager();
    private final Repository repository = Core.getInstance().getRepository();

    @Override
    public Profile getContext(BukkitCommandExecutionContext bukkitCommandExecutionContext) throws InvalidCommandArgument {
        var username = bukkitCommandExecutionContext.popFirstArg();
        var profileInCache = profileManager.getByUsername(username);
        if (profileInCache.isPresent()) {
            return profileInCache.get();
        } else {
            var profileInRepository = await(repository.getProfileStore().getByUsername(username));
            return profileInRepository.orElseThrow(() -> new InvalidCommandArgument(String.format("Invalid profile! Has %s logged on the server?", username)));
        }
    }
}

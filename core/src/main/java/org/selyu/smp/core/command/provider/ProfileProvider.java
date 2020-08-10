package org.selyu.smp.core.command.provider;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.selyu.commands.api.argument.CommandArg;
import org.selyu.commands.api.exception.CommandExitMessage;
import org.selyu.commands.api.parametric.CommandProvider;
import org.selyu.smp.core.Core;
import org.selyu.smp.core.data.Repository;
import org.selyu.smp.core.manager.ProfileManager;
import org.selyu.smp.core.profile.Profile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.selyu.smp.core.util.MessageUtil.error;

public final class ProfileProvider extends CommandProvider<Profile> {
    private final ProfileManager profileManager = Core.getInstance().getProfileManager();
    private final Repository repository = Core.getInstance().getRepository();

    @Override
    public boolean doesConsumeArgument() {
        return true;
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Nullable
    @Override
    public Profile provide(@Nonnull CommandArg arg, @Nonnull List<? extends Annotation> annotations) throws CommandExitMessage {
        Optional<Profile> profileInCache = profileManager.getByUsername(arg.get());
        if (profileInCache.isPresent()) {
            return profileInCache.get();
        } else {
            Optional<Profile> profileInRepository = repository.getProfileStore().getByUsername(arg.get()).join();
            return profileInRepository.orElseThrow(() -> new CommandExitMessage(error("Cant find profile by name " + arg.get())));
        }
    }

    @Override
    public String argumentDescription() {
        return "player";
    }

    @Override
    public List<String> getSuggestions(@Nonnull String prefix) {
        return Bukkit.getServer().getOnlinePlayers()
                .stream()
                .map(Player::getName)
                .filter(name -> name.startsWith(prefix))
                .collect(Collectors.toList());
    }
}

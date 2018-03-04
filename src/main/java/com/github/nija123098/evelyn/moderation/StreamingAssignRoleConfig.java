package com.github.nija123098.evelyn.moderation;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.wrappers.*;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordPresenceUpdate;
import com.github.nija123098.evelyn.exception.ArgumentException;
import com.github.nija123098.evelyn.launcher.Launcher;

import java.util.HashSet;
import java.util.Set;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class StreamingAssignRoleConfig extends AbstractConfig<Role, Guild> {
    public StreamingAssignRoleConfig() {
        super("streaming_role", "Streamer Role", ConfigCategory.MODERATION, (Role) null, "The role to assign a streaming user");
        Launcher.registerAsyncStartup(() -> DiscordClient.getUsers().forEach(user -> check(user, null)));
    }

    @EventListener
    public void handle(DiscordPresenceUpdate event) {
        check(event.getUser(), event.getOldPresence());
    }

    private void check(User user, Presence oldPresence) {
        if (user.getPresence().getOptionalStreamingUrl().isPresent()) {
            rolesForGuilds(user.getGuilds()).forEach(role -> {
                try {
                    user.addRole(role);
                } catch (Exception ignored) {
                }// todo role order moved
            });
        } else if (oldPresence == null || oldPresence.getOptionalStreamingUrl().isPresent()) {
            rolesForGuilds(user.getGuilds()).forEach(role -> {
                try {
                    user.removeRole(role);
                } catch (Exception ignored) {
                }// more role order moved here
            });
        }

    }

    private static Set<Role> rolesForGuilds(Set<Guild> guilds) {
        Set<Role> roles = new HashSet<>(guilds.size() / 20 + 2);
        guilds.forEach(guild -> {
            Role role = ConfigHandler.getSetting(StreamingAssignRoleConfig.class, guild);
            if (role != null) roles.add(role);
        });
        return roles;
    }

    @Override
    protected Role validateInput(Guild configurable, Role role) {
        if (role.getPosition() <= DiscordClient.getOurUser().getRolesForGuild(configurable).get(0).getPosition())
            throw new ArgumentException("For me to assign roles I must have a higher role");
        return role;
    }
}

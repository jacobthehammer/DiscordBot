package com.github.nija123098.evelyn.perms;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventDistributor;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordUserRolesUpdateEvent;
import com.github.nija123098.evelyn.launcher.Launcher;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class ContributorMonitor {
    private static final Role CONTRIB_SIGN_ROLE = Role.getRole(ConfigProvider.BOT_SETTINGS.contributorSignRole());
    private static final Role SUPPORT_SIGN_ROLE = Role.getRole(ConfigProvider.BOT_SETTINGS.supporterSignRole());
    public static void init() {
        if (CONTRIB_SIGN_ROLE == null) return;// is not the instance serving Evelyn's Space
        EventDistributor.register(ContributorMonitor.class);
        Launcher.registerStartup(() -> {
            reload(SUPPORT_SIGN_ROLE, BotRole.SUPPORTER);
            reload(CONTRIB_SIGN_ROLE, BotRole.CONTRIBUTOR);
        });
    }
    private static void reload(Role role, BotRole botRole) {
        role.getGuild().getUsers().forEach(user -> {
            if (user.getRolesForGuild(role.getGuild()).contains(role)) {
                if (botRole.hasRole(user, null)) BotRole.setRole(BotRole.SUPPORTER, true, user, null);
            } else if (botRole.hasRole(user, null)) BotRole.setRole(BotRole.SUPPORTER, false, user, null);
        });
    }
    @EventListener
    public static void handle(DiscordUserRolesUpdateEvent event) {
        handle(event, SUPPORT_SIGN_ROLE, BotRole.SUPPORTER);
        handle(event, CONTRIB_SIGN_ROLE, BotRole.CONTRIBUTOR);
    }
    public static void handle(DiscordUserRolesUpdateEvent event, Role role, BotRole botRole) {
        if (event.newRoles().contains(role) && !event.oldRoles().contains(role)) BotRole.setRole(botRole, true, event.getUser(), null);
        else if (!event.newRoles().contains(role) && event.oldRoles().contains(role)) BotRole.setRole(botRole, false, event.getUser(), null);
    }
}

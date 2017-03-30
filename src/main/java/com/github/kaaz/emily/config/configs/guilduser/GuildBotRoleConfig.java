package com.github.kaaz.emily.config.configs.guilduser;

import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.config.Configurable;
import com.github.kaaz.emily.perms.BotRole;

import java.util.ArrayList;
import java.util.List;

/**
 * Made by nija123098 on 2/22/2017.
 */
public class GuildBotRoleConfig extends AbstractConfig<List<String>, Configurable.GuildUser> {
    public GuildBotRoleConfig() {
        super("guild_flag_ranks", BotRole.GUILD_OWNER, new ArrayList<>(2), "Flags available to guild specific bot roles");
    }
}

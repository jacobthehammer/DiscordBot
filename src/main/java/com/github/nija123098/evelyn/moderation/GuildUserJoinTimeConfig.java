package com.github.nija123098.evelyn.moderation;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordUserJoin;

import static com.github.nija123098.evelyn.config.ConfigCategory.STAT_TRACKING;
import static com.github.nija123098.evelyn.config.GuildUser.getGuildUser;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class GuildUserJoinTimeConfig extends AbstractConfig<Long, GuildUser> {
    public GuildUserJoinTimeConfig() {
        super("user_join_time", "", STAT_TRACKING, guildUser -> guildUser.getGuild().getJoinTimeForUser(guildUser.getUser()), "The first time a user joins a guild");
        config = this;
    }

    private static GuildUserJoinTimeConfig config;

    public static long get(GuildUser guildUser) {
        Long aLong = config.getValue(guildUser, false);
        if (aLong == null) {
            aLong = guildUser.getGuild().getJoinTimeForUser(guildUser.getUser());
            config.setValue(guildUser, aLong, false);
        }
        return aLong;
    }

    @EventListener
    public void handle(DiscordUserJoin event) {
        GuildUser guildUser = getGuildUser(event.getGuild(), event.getUser());
        if (this.getValue(guildUser, false) != null) return;
        this.setValue(guildUser, event.getJoinTime(), false);
    }
}
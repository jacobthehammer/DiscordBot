package com.github.kaaz.emily.discordobjects.wrappers.event.events;

import com.github.kaaz.emily.discordobjects.wrappers.Guild;
import sx.blah.discord.handle.impl.events.GuildLeaveEvent;

/**
 * Made by nija123098 on 3/31/2017.
 */
public class DiscordGuildLeave {
    private GuildLeaveEvent event;
    public DiscordGuildLeave(GuildLeaveEvent event) {
        this.event = event;
    }
    public Guild getGuild(){
        return Guild.getGuild(this.event.getGuild());
    }
}

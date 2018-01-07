package com.github.nija123098.evelyn.information.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordGuildJoin;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordGuildLeave;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordMessageSend;
import com.github.nija123098.evelyn.moderation.logging.BotChannelConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class GuildIsNewConfig extends AbstractConfig<Boolean, Guild> {
    private static final Set<Guild> GUILDS = new HashSet<>();
    public GuildIsNewConfig() {
        super("guild_is_new", "", ConfigCategory.STAT_TRACKING, true, "If the guild had been served previously");
    }
    @EventListener
    public void handle(DiscordMessageSend send){
        if (send.getChannel().isPrivate()) return;
        welcome(send.getGuild());
    }
    @EventListener
    public void handle(DiscordGuildJoin join){
        welcome(join.getGuild());
    }
    @EventListener
    public void handle(DiscordGuildLeave leave){
        GUILDS.remove(leave.getGuild());
        this.reset(leave.getGuild());
    }
    private void welcome(Guild guild){
        if (!GUILDS.add(guild)) return;
        if (!this.getValue(guild, false)) return;
        this.setValue(guild, false, false);
        Channel channel = ConfigHandler.getSetting(BotChannelConfig.class, guild);
        MessageMaker maker = new MessageMaker(channel == null ? guild.getGeneralChannel() != null ? guild.getGeneralChannel() : guild.getChannels().get(0) : channel);
        if (channel == null){
            maker.append("Thank you for adding me to this server!\nI always respond to being mentioned!  To change the default `!` prefix do @Evelyn prefix `new prefix`.\nI also come with a `@Evelyn guide`").mustEmbed().send();
        }
    }
}

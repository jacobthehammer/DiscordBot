package com.github.nija123098.evelyn.moderation.messagefiltering.commands;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.moderation.messagefiltering.MessageMonitoringLevel;
import com.github.nija123098.evelyn.moderation.messagefiltering.configs.MessageMonitoringAdditionsConfig;
import com.github.nija123098.evelyn.moderation.messagefiltering.configs.MessageMonitoringConfig;
import com.github.nija123098.evelyn.moderation.messagefiltering.configs.MessageMonitoringExceptionsConfig;
import com.github.nija123098.evelyn.moderation.messagefiltering.configs.MessageMonitoringUrlWhitelist;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class MessageMonitoringUrlWhitelistCommand extends AbstractCommand {
    public MessageMonitoringUrlWhitelistCommand() {
        super("urlwhitelist", ModuleLevel.ADMINISTRATIVE, "mmurlwl, urlwhite, linkwhitelist, linkwhite", null, "Displays a list of whitelisted URLs");
    }
    @Command
    public static void command(Guild guild, Channel channel, String in, MessageMaker maker) {
        if (in != null && !in.isEmpty()) {
            MessageMonitoringUrlWhitelistAddCommand.command(guild, channel, in, maker);
            return;// prevent two whitelist listings
        }
        maker.getTitle().append("URL Whitelist");
        if (!((ConfigHandler.getSetting(MessageMonitoringConfig.class, channel.getGuild()).contains(MessageMonitoringLevel.URL) && !ConfigHandler.getSetting(MessageMonitoringExceptionsConfig.class, channel).contains(MessageMonitoringLevel.URL)) || !ConfigHandler.getSetting(MessageMonitoringAdditionsConfig.class, channel).contains(MessageMonitoringLevel.URL))) {
            maker.append("URL message monitoring must be enabled for this to work!\n\n");
        }
        ConfigHandler.getSetting(MessageMonitoringUrlWhitelist.class, channel.getGuild()).forEach(s -> maker.getNewListPart().appendRaw(s));
    }
}

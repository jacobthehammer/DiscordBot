package com.github.nija123098.evelyn.information;

import com.github.nija123098.evelyn.moderation.GuildUserJoinTimeConfig;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Context;
import com.github.nija123098.evelyn.command.configs.CommandsUsedCountConfig;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.economy.configs.CurrentMoneyConfig;
import com.github.nija123098.evelyn.util.EmoticonHelper;
import com.github.nija123098.evelyn.util.Time;

/**
 * Made by nija123098 on 5/21/2017.
 */
public class UserCommand extends AbstractCommand {
    public UserCommand() {
        super("user", ModuleLevel.INFO, "whois", null, "Shows information about the user");
    }
    @Command
    public void command(@Argument(optional = true) User user, @Context(softFail = true) Guild guild, MessageMaker maker){
        maker.appendAlternate(false, "Querying for **", user.getDisplayName(guild) + "**\n").withImage(user.getAvatarURL()).withColor(user.getAvatarURL());
        addAtrib(maker, "bust_in_silhouette", "User", user.getNameAndDiscrim());
        addAtrib(maker, "id", "Discord id", user.getID());
        addAtrib(maker, "keyboard", "Commands used", ConfigHandler.getSetting(CommandsUsedCountConfig.class, user) + "");
        addAtrib(maker, "cookie", "Cookies", ConfigHandler.getSetting(CurrentMoneyConfig.class, user) + "");
        addAtrib(maker, "date", "Joined server", Time.getAbbreviated(System.currentTimeMillis() - GuildUserJoinTimeConfig.get(GuildUser.getGuildUser(guild, user))) + " ago");
        addAtrib(maker, "calendar_spiral", "Joined Discord", Time.getAbbreviated(System.currentTimeMillis() - user.getJoinDate()) + " ago");
    }
    private static void addAtrib(MessageMaker maker, String icon, String info, String content){
        maker.appendAlternate(true, EmoticonHelper.getChars(icon, false), "  " + info, ": " + content + "\n");
    }
}
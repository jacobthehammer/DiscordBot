package com.github.nija123098.evelyn.config.commands;

import com.github.nija123098.evelyn.audio.Playlist;
import com.github.nija123098.evelyn.audio.Track;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.command.annotations.Context;
import com.github.nija123098.evelyn.config.*;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.exeption.DevelopmentException;
import com.github.nija123098.evelyn.exeption.PermissionsException;

/**
 * Made by nija123098 on 4/2/2017.
 */
public class ConfigSetCommand extends AbstractCommand {
    public ConfigSetCommand() {
        super(ConfigCommand.class, "set", null, null, null, "Sets");
    }
    @Command
    public <T extends Configurable> void command(@Argument AbstractConfig<?, T> config, @Argument(optional = true) T target, String arg, @Context(softFail = true) Track track, @Context(softFail = true) Playlist playlist, User user, Channel channel, @Context(softFail = true) GuildUser guildUser, @Context(softFail = true) Guild guild){
        if (!config.requiredBotRole().hasRequiredRole(user, guild)){
            throw new PermissionsException("You must be at least a " + config.requiredBotRole().name() + " to edit that config");
        }
        if (arg.isEmpty()){
            ConfigHandler.setSetting((Class<? extends AbstractConfig<T,Configurable>>) config.getClass(), guild == null ? user : guild,  (T) target.convert((Class<T>) config.getValueType()));
        }else{
            if (target == null) target = (T) new Configurable[]{track, playlist, user, channel, guildUser, target instanceof Role ? target : null, guild, GlobalConfigurable.GLOBAL, target}[config.getConfigLevel().ordinal()].convert(config.getConfigLevel().getType());
            target.checkPermissionToEdit(user, guild);// morph exception should throw before cast exception
            ConfigHandler.setExteriorSetting(config.getClass(), target, arg);
        }
    }
}

package com.github.kaaz.emily.helping.presence;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.config.ConfigHandler;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.discordobjects.wrappers.User;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Made by nija123098 on 6/23/2017.
 */
public class BackCommand extends AbstractCommand {
    public BackCommand() {
        super(PresenceCommand.class, "back", "present", null, null, "Marks you as no longer afk");
    }
    @Command
    public void command(MessageMaker maker, User user){
        ConfigHandler.setSetting(SelfMarkedAwayConfig.class, user, false);
        maker.append("Welcome back!");
    }
    private static final Set<String> NATURAL_TRIGGERS = new HashSet<>(Arrays.asList("back", "present", "i am back", "i'm back", "present", "back"));
    @Override
    public Set<String> getNaturalTriggers(){
        return NATURAL_TRIGGERS;
    }
    @Override
    public boolean prefixRequired() {
        return true;
    }
}

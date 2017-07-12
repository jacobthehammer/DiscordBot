package com.github.kaaz.emily.automoderation;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ContextType;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.annotations.Argument;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.wrappers.Channel;

import java.util.stream.Collectors;

/**
 * Made by nija123098 on 5/10/2017.
 */
public class PurgeCommand extends AbstractCommand {
    public PurgeCommand() {
        super("purge", ModuleLevel.ADMINISTRATIVE, null, null, "Deletes old messages");
    }
    @Command
    public void command(@Argument(optional = true, replacement = ContextType.NONE) Integer count, Channel channel){
        if (count == null) count = 100;
        if (count > 2499) count = 2499;
        MessageDeleteService.delete(channel.getMessages(count + 1).stream().filter(message -> !message.isPinned()).collect(Collectors.toList()));
    }
}

package com.github.kaaz.emily.audio.commands.current;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.audio.Track;

/**
 * Made by nija123098 on 5/31/2017.
 */
public class CurrentSourceCommand extends AbstractCommand {
    public CurrentSourceCommand() {
        super(CurrentCommand.class, "source", null, null, null, "Shows the source of the video");
    }
    @Command
    public void command(Track track, MessageMaker maker){
        maker.append(track.getSource());
    }
}

package com.github.kaaz.emily.audio.commands.current;

import com.github.kaaz.emily.audio.Track;
import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.guildaudiomanager.GuildAudioManager;
import com.github.kaaz.emily.discordobjects.wrappers.Channel;
import com.github.kaaz.emily.service.services.ScheduleService;

/**
 * Made by nija123098 on 5/31/2017.
 */
public class CurrentUpdateTitleCommand extends AbstractCommand {
    public CurrentUpdateTitleCommand() {
        super(CurrentCommand.class, "updatetitle", null, null, null, "updates the topic of the music channel every 10 seconds");
    }
    @Command
    public void command(GuildAudioManager manager, Channel channel){
        ScheduleService.scheduleRepeat(0, 10_000, () -> {
            Track track;
            if ((track = manager.currentTrack()) != null){
                Long length = track.getLength();
                if (length == null) channel.changeTopic("Stream: " + track.getName());
                else channel.changeTopic(CurrentCommand.getPlayBar(manager.isPaused(), manager.currentTime(), length) + " " + manager.currentTrack().getName());
            }
        });
    }
}

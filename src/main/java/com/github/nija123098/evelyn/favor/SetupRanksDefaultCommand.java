package com.github.nija123098.evelyn.favor;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.favor.configs.balencing.TimeFavorFactorConfig;

import java.awt.Color;

public class SetupRanksDefaultCommand extends AbstractCommand {
    public SetupRanksDefaultCommand() {
        super(SetupRanksCommand.class, "default", null, null, null, "Sets up ranks only by time");
    }
    @Command
    public void command(MessageMaker maker, Guild guild){
        ConfigHandler.getConfigs().stream().filter(abstractConfig -> abstractConfig.getName().equals("favor_factor")).forEach(abstractConfig -> ((AbstractConfig<Float, Guild>) abstractConfig).setValue(guild, 0F));
        ConfigHandler.setSetting(TimeFavorFactorConfig.class, guild, .01F);
        SetupRanksCommand.command(maker, guild, true, Color.GREEN, new Color(0x8500ff), "x^2.5");
    }
}

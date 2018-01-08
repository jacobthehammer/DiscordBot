package com.github.nija123098.evelyn.fun.starboard;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

/**
 * Made by nija123098 on 5/31/2017.
 */
public class StarBoardLocationConfig extends AbstractConfig<Channel, Guild> {
    public StarBoardLocationConfig() {
        super("current_money", "star_board", ConfigCategory.LOGGING, (Channel) null, "The location of the starboard");
    }
}

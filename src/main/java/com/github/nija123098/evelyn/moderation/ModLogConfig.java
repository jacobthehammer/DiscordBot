package com.github.nija123098.evelyn.moderation;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.perms.BotRole;

/**
 * Made by nija123098 on 5/10/2017.
 */
public class ModLogConfig extends AbstractConfig<Channel, Guild> {
    public ModLogConfig() {
        super("mod_log", ConfigCategory.LOGGING, (Channel) null, "The channel log of moderator actions");
    }
}

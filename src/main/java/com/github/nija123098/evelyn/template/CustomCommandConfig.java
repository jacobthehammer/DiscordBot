package com.github.nija123098.evelyn.template;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

import java.util.ArrayList;
import java.util.List;

/**
 * Made by nija123098 on 8/11/2017.
 */
public class CustomCommandConfig extends AbstractConfig<List<CustomCommand>, Guild> {
    public CustomCommandConfig() {
        super("current_money", "custom_commands", ConfigCategory.STAT_TRACKING, new ArrayList<>(0), "The map of custom command by name then functions");
    }
}

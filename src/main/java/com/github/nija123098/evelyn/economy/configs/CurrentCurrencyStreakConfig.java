package com.github.nija123098.evelyn.economy.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class CurrentCurrencyStreakConfig extends AbstractConfig<Integer, User> {
    public CurrentCurrencyStreakConfig() {
        super("current_streak", "", ConfigCategory.STAT_TRACKING, 0, "streak in days where user has used claim command");
    }
}

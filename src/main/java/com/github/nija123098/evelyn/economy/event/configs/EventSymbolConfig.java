package com.github.nija123098.evelyn.economy.event.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.GlobalConfigurable;

/**
 * @author Soarnir
 * @since 1.0.0
 */
public class EventSymbolConfig extends AbstractConfig<String, GlobalConfigurable> {
    public EventSymbolConfig() {
        super("event_symbol", "", ConfigCategory.STAT_TRACKING, "", "event symbol");
    }
}

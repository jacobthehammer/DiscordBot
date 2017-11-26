package com.github.nija123098.evelyn.economy.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.exception.ArgumentException;
import com.github.nija123098.evelyn.util.EmoticonHelper;

/**
 * Made by nija123098 on 5/1/2017.
 */
public class CurrencySymbolConfig extends AbstractConfig<String, Guild> {
    public CurrencySymbolConfig() {
        super("currency_symbol", "", ConfigCategory.ECONOMY, "cookie", "The symbol currency is represented by");
    }

    @Override
    public String setValue(Guild configurable, String value, boolean overrideCache) {
        if (EmoticonHelper.getChars(value.toLowerCase(), false) != null) value = EmoticonHelper.getChars(value, false);
        if (EmoticonHelper.getName(value) == null) throw new ArgumentException("Unknown emote. Please choose a valid Unicode emote.");
        return super.setValue(configurable, EmoticonHelper.getName(value), true);
    }

    @Override
    public String getValue(Guild configurable) {
        return EmoticonHelper.getChars(super.getValue(configurable), false);
    }
}

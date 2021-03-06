package com.github.nija123098.evelyn.information.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordUserNameChangeEvent;
import com.github.nija123098.evelyn.exception.DevelopmentException;
import com.github.nija123098.evelyn.util.Log;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class OldUserNameConfig extends AbstractConfig<Set<String>, User> {
    public OldUserNameConfig() {
        super("old_name_config", "", ConfigCategory.STAT_TRACKING, new HashSet<>(0), "A list of old names used by a user");
    }
    @EventListener
    public void handle(DiscordUserNameChangeEvent event) {
        try {
            this.alterSetting(event.getUser(), strings -> {
                strings.add(event.getOldName());
                strings.add(event.getNewName());
            });
        } catch (DevelopmentException e) {
            if (!(e.getCause() instanceof SQLException)) throw e;
            Log.log("Unable to insert username change for user " + event.getUser().getID() + " from " + event.getOldName() + " to " + event.getNewName());
        }
    }
}

package com.github.nija123098.evelyn.favor.configs;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.favor.FavorChangeEvent;
import com.github.nija123098.evelyn.perms.BotRole;
import com.google.common.util.concurrent.AtomicDouble;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Made by nija123098 on 5/6/2017.
 */
public class EarnRankConfig extends AbstractConfig<Float, Role> {
    public EarnRankConfig() {
        super("favor_requirement", ConfigCategory.FAVOR, (Float) null, "A map of roles earned by users due to their favor in a guild");
    }
    @EventListener
    public void handle(FavorChangeEvent event){// change to config change event
        if (!event.getConfigurable().getClass().equals(GuildUser.class)) return;
        GuildUser user = (GuildUser) event.getConfigurable();
        Set<Role> roles = user.getGuild().getRoles().stream().filter(role -> this.getValue(role) != null).collect(Collectors.toSet());
        Set<Role> independents = roles.stream().filter(role -> ConfigHandler.getSetting(StackFavorRankConfig.class, role)).collect(Collectors.toSet());
        independents.forEach(role -> {
            if (this.getValue(role) > event.getNewValue()) user.getUser().removeRole(role);
            else user.getUser().addRole(role);
        });
        roles.removeAll(independents);
        AtomicDouble highest = new AtomicDouble(-1);
        AtomicReference<Role> highestRole = new AtomicReference<>();
        roles.forEach(role -> {
            float val = this.getValue(role);
            if (val < event.getNewValue() && val > highest.get()) {
                highest.set(val);
                highestRole.set(role);
            }
        });
        if (highest.get() == -1) return;
        roles.forEach(role -> {
            if (!role.equals(highestRole.get())) user.getUser().removeRole(role);
        });
        user.getUser().addRole(highestRole.get());
    }
}
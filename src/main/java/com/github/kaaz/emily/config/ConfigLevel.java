package com.github.kaaz.emily.config;

import com.github.kaaz.emily.discordobjects.wrappers.*;

/**
 * The enum to represent a type of configurable object.
 * Objects that can be configured must have a type.
 *
 * @author nija123098
 * @since 2.0.0
 */
public enum ConfigLevel {
    /** The type for audio tracks */
    TRACK(Track.class),
    /** The type for any playlist type */
    PLAYLIST(Playlist.class),
    /** The type for a user's config */
    USER(User.class),
    /** The type for a channel's config */
    CHANNEL(Channel.class),
    /** The type for a user's config within a guild*/
    GUILD_USER(Configurable.GuildUser.class),
    /** The type for a role within a guild */
    ROLE(Role.class),
    /** The type for a guild's config */
    GUILD(Guild.class),
    /** The type for global config */
    GLOBAL(Configurable.GlobalConfigurable.class),;
    private Class<? extends Configurable> clazz;
    ConfigLevel(Class<? extends Configurable> clazz) {
        this.clazz = clazz;
    }
    public Class<? extends Configurable> getType(){
        return this.clazz;
    }
    public static ConfigLevel getLevel(Class<? extends Configurable> clazz){
        for (ConfigLevel level : values()){
            if (level.clazz.isAssignableFrom(clazz)){
                return level;
            }
        }
        if (clazz.equals(Configurable.class)){
            return null;
        }
        throw new RuntimeException("Class does not have a type: " + clazz.getName());
    }
}

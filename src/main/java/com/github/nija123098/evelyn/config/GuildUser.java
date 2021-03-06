package com.github.nija123098.evelyn.config;

import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.User;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventDistributor;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventListener;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordUserJoin;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordUserLeave;
import com.github.nija123098.evelyn.exception.ConfigurableConvertException;
import com.github.nija123098.evelyn.exception.DException;
import com.github.nija123098.evelyn.perms.BotRole;
import sx.blah.discord.handle.obj.IUser;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The configurable for users in relation to a guild.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class GuildUser implements Configurable {
    /**
     * The map containing guild user configurables.
     */
    private static final Map<String, GuildUser> ID_CACHE = new ConcurrentHashMap<>();// if these are changed to remove after an amount of time
    private static final Map<Guild, Map<User, String>> GUILD_MAP_CACHE = new ConcurrentHashMap<>();// ordering will need to be redone.
    private static final Set<Guild> ORDERED = new HashSet<>();

    static {
        EventDistributor.register(GuildUser.class);
    }

    @EventListener
    public static void handle(DiscordUserJoin join) {
        if (!ORDERED.contains(join.getGuild())) return;
        getGuildUser(join.getGuild(), join.getUser()).number = join.getGuild().getUserSize() - 1;
    }

    @EventListener
    public static void handle(DiscordUserLeave leave) {
        ORDERED.remove(leave.getGuild());
        new GuildUser(leave.getGuild(), leave.getUser()).invalidate();
    }

    /**
     * The getter for a object that represents a guild user.
     *
     * @param id the id of the guild user.
     * @return the guild user object for the guild and user.
     */
    public static GuildUser getGuildUser(String id) {
        GuildUser ret = ID_CACHE.computeIfAbsent(id, s -> {
            if (!id.startsWith("gu-")) return null;
            String[] split = id.split("-id-");
            Guild guild = Guild.getGuild(split[0].substring(3));
            User user = User.getUser(split[1]);
            if (guild == null || user == null) return null;
            GuildUser guildUser = new GuildUser(guild, user);
            GUILD_MAP_CACHE.computeIfAbsent(guild, g -> new ConcurrentHashMap<>()).put(user, guildUser.getID());
            return guildUser;
        });
        if (ret != null && !ret.isValid()) {
            ret.invalidate();
            return null;
        }
        return ret;
    }

    /**
     * The getter for a object that represents a guild user.
     *
     * @param guild the guild for a guild user object.
     * @param user the user for the guild object.
     * @return the guild user object for the guild and user.
     */
    public static GuildUser getGuildUser(Guild guild, User user) {
        if (user == null || guild == null) return null;
        GuildUser ret = ID_CACHE.get(GUILD_MAP_CACHE.computeIfAbsent(guild, g -> new ConcurrentHashMap<>()).computeIfAbsent(user, u -> {
            GuildUser guildUser = new GuildUser(guild, u);
            ID_CACHE.put(guildUser.getID(), guildUser);
            return guildUser.getID();
        }));
        if (ret != null && !ret.isValid()) {
            ret.invalidate();
            return null;
        }
        return ret;
    }

    /**
     * Orders the {@link GuildUser}s and sets the
     * {@link GuildUser#number} to the order
     * which the user joined.
     *
     * @param guild the guild to order {@link GuildUser}s for.
     */
    private static void orderGuildUsers(Guild guild) {
        AtomicInteger atomicInteger = new AtomicInteger();
        guild.getUsers().stream().map(user -> GuildUser.getGuildUser(guild, user)).sorted((first, second) -> (int) (first.getJoinTime() - second.getJoinTime())).filter(GuildUser::isValid).forEach(guildUser -> guildUser.number = atomicInteger.getAndIncrement());
    }
    private static void ensureOrdering(Guild guild) {
        if (!ORDERED.add(guild)) return;
        orderGuildUsers(guild);
    }

    private transient int number = -1;
    private String id;
    private Guild guild;
    private User user;
    protected GuildUser() {}
    private GuildUser(Guild guild, User user) {
        this.id = "gu-" + guild.getID() + "-id-" + user.getID();
        this.guild = guild;
        this.user = user;
        this.registerExistence();
    }
    @Override
    public String getID() {
        return this.id;
    }

    /**
     * Returns the position in which the user joined..
     *
     * @return the position in which the user joined.
     */
    public int getJoinPosition() {
        ensureOrdering(this.guild);
        return this.number;
    }
    public long getJoinTime() {
        try {
            return this.guild.getJoinTimeForUser(this.user);
        } catch (DException e) {
            return -1;
        }
    }
    @Override
    public String getName() {
        return getUser().getDisplayName(getGuild());
    }
    @Override
    public ConfigLevel getConfigLevel() {
        return ConfigLevel.GUILD_USER;
    }
    public void checkPermissionToEdit(User user, Guild guild) {
        if (user.equals(this.getUser())) {
            return;
        }
        BotRole.GUILD_TRUSTEE.checkRequiredRole(user, guild);
    }
    @Override
    public Configurable getGoverningObject() {
        return getGuild();
    }
    @Override
    public <T extends Configurable> Configurable convert(Class<T> t) {
        if (t.equals(GuildUser.class)) return this;
        if (t.equals(Guild.class)) return this.getGuild();
        if (t.equals(User.class)) return this.getUser();
        throw new ConfigurableConvertException(this.getClass(), t);
    }
    @Override
    public boolean equals(Object o) {
        return Configurable.class.isInstance(o) && this.getID().equals(((Configurable) o).getID());
    }

    /**
     * Returns the guild which the guild user is a member of.
     *
     * @return the guild which the guild user is a member of.
     */
    public Guild getGuild() {
        return this.guild;
    }

    /**
     * Returns the user which is the member of a guild.
     *
     * @return the user which is the member of a guild.
     */
    public User getUser() {
        return this.user;
    }

    /**
     * Checks if the instance is still valid.
     * A instance is invalid if the guild no longer exists
     * or if the user is no longer in the guild.
     *
     * @return if the instance is still valid.
     */
    public boolean isValid() {
        List<IUser> users = this.guild.guild().getUsers();
        return users.contains(DiscordClient.getOurUser().user()) && users.contains(this.user.user());
    }

    /**
     * Removes this instance from the caches.
     */
    public void invalidate() {
        Map<User, String> map = GUILD_MAP_CACHE.get(this.guild);
        if (map != null) {
            map.remove(this.user);
            if (map.isEmpty()) GUILD_MAP_CACHE.remove(this.guild);
        }
        ID_CACHE.remove(this.getID());
    }
}

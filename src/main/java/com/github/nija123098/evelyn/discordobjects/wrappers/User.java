package com.github.nija123098.evelyn.discordobjects.wrappers;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.config.ConfigLevel;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.discordobjects.ExceptionWrapper;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.EventDistributor;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordUserJoin;
import com.github.nija123098.evelyn.discordobjects.wrappers.event.events.DiscordUserLeave;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.util.ConcurrentLoadingHashMap;
import com.github.nija123098.evelyn.util.FormatHelper;
import com.github.nija123098.evelyn.util.StringHelper;
import com.github.nija123098.evelyn.util.Time;
import sx.blah.discord.handle.obj.IUser;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Wraps a Discord4j {@link IUser} object.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class User implements Configurable {
    public static final ConcurrentLoadingHashMap<IUser, User> CACHE = new ConcurrentLoadingHashMap<>(ConfigProvider.CACHE_SETTINGS.userSize(), User::new);
    public static User getUser(String id) {
        try {
            IUser iUser = DiscordClient.getAny(client -> client.getUserByID(Long.parseLong(FormatHelper.filtering(id, Character::isLetterOrDigit))));;
            if (iUser == null) return null;
            return CACHE.get(iUser);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    public static User getUser(IUser user) {
        if (user == null) return null;
        return CACHE.get(user);
    }
    static List<User> getUsers(Collection<IUser> iUsers) {
        List<User> users = new ArrayList<>(iUsers.size());
        iUsers.forEach(iUser -> users.add(getUser(iUser)));
        return users;
    }
    public static void update(IUser user) {// hash is based on id, so no old channel is necessary
        User u = CACHE.getIfPresent(user);
        if (u != null) u.reference.set(user);
    }
    private transient final AtomicReference<IUser> reference;
    private String ID;
    protected User() {
        this.reference = new AtomicReference<>(DiscordClient.getAny(client -> client.getUserByID(Long.parseLong(ID))));
    }
    User(IUser user) {
        this.reference = new AtomicReference<>(user);
        this.ID = this.reference.get().getStringID();
        this.registerExistence();
    }
    public IUser user() {
        return reference.get();
    }
    @Override
    public String getID() {
        return user().getStringID();
    }
    @Override
    public ConfigLevel getConfigLevel() {
        return ConfigLevel.USER;
    }

    @Override
    public void checkPermissionToEdit(User user, Guild guild) {
        if (!this.equals(user)) {
            BotRole.BOT_ADMIN.checkRequiredRole(user, null);
        }
    }

    @Override
    public int hashCode() {
        return this.user().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof User && this.getID().equals(((User) o).getID());
    }

    public String getName() {
        return user().getName();
    }

    @Override
    public void manage() {// this should probably be more dependent on constants
        /*if (ConfigHandler.getSetting(LastCurrencyUseConfig.class, this) > System.currentTimeMillis() - 86_400_000) {
            ConfigHandler.setSetting(LastCurrencyUseConfig.class, this, 0L);
        }*/
    }

    static {
        EventDistributor.register(User.class);
    }
    private transient Set<Guild> guilds;
    public Set<Guild> getGuilds() {
        if (this.guilds == null) this.guilds = DiscordClient.getGuilds().stream().filter(guild -> guild.getUsers().contains(this)).collect(Collectors.toSet());
        return guilds;
    }

    public static void handle(DiscordUserJoin join) {
        if (join.getUser().guilds != null) join.getUser().guilds.add(join.getGuild());
    }

    public static void handle(DiscordUserLeave leave) {
        if (leave.getUser().guilds != null) leave.getUser().guilds.add(leave.getGuild());
    }

    public String getNameAndDiscrim() {
        return getName() + "#" + getDiscriminator();
    }
    public String getFriendlyName() {
        return StringHelper.makeUserFriendlyName(this.getName());
    }

    public String getAvatar() {
        return user().getAvatar();
    }

    public String getAvatarURL() {
        return user().getAvatarURL();
    }

    public long getJoinDate() {
        return Time.toMillis(this.user().getCreationDate());
    }

    public Presence getPresence() {
        return Presence.getPresence(user().getPresence());
    }

    public String getDisplayName(Guild guild) {
        return guild == null ? user().getName() : user().getDisplayName(guild.guild());
    }

    public String getFriendlyDisplayName(Guild guild) {
        return StringHelper.makeUserFriendlyName(this.getDisplayName(guild));
    }

    public String getNickname(Guild guild) {
        String nick = this.getDisplayName(guild);
        return this.getName().equals(nick) ? null : nick;
    }

    public String geFriendlyNickname(Guild guild) {
        return StringHelper.makeUserFriendlyName(this.getNickname(guild));
    }

    public String mention() {
        return mention(true);
    }

    public String mention(boolean mentionWithNickname) {
        return user().mention(mentionWithNickname);
    }

    public String getDiscriminator() {
        return user().getDiscriminator();
    }

    public List<Role> getRolesForGuild(Guild guild) {
        return Role.getRoles(user().getRolesForGuild(guild.guild()));
    }

    public EnumSet<DiscordPermission> getPermissionsForGuild(Guild guild) {
        return DiscordPermission.getDiscordPermissions(user().getPermissionsForGuild(guild.guild()));
    }

    public boolean isBot() {
        return user().isBot();
    }

    public VoiceChannel getConnectedVoiceChannel(Guild guild) {
        return VoiceChannel.getVoiceChannel(this.user().getVoiceStateForGuild(guild.guild()).getChannel());
    }

    public void moveToVoiceChannel(VoiceChannel newChannel) {
        ExceptionWrapper.wrap(() -> user().moveToVoiceChannel(newChannel.channel()));
    }

    public DirectChannel getOrCreatePMChannel() {
        return ExceptionWrapper.wrap((ExceptionWrapper.Request<DirectChannel>) () -> DirectChannel.getDirectChannel(user().getOrCreatePMChannel()));
    }

    public boolean isDeaf(Guild guild) {
        return user().getVoiceStateForGuild(guild.guild()).isDeafened();
    }

    public boolean isMuted(Guild guild) {
        return user().getVoiceStateForGuild(guild.guild()).isMuted();
    }

    public void addRole(Role role) {
        if (role.getUsers().contains(this)) return;
        ExceptionWrapper.wrap(() -> user().addRole(role.role()));
    }

    public void removeRole(Role role) {
        if (!role.getUsers().contains(this)) return;
        ExceptionWrapper.wrap(() -> user().removeRole(role.role()));
    }

    public long getLongID() {
        return user().getLongID();
    }
}

package com.github.nija123098.evelyn.discordobjects.wrappers;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.discordobjects.ExceptionWrapper;
import com.github.nija123098.evelyn.exception.GhostException;
import com.github.nija123098.evelyn.exception.PermissionsException;
import com.github.nija123098.evelyn.util.Cache;
import com.github.nija123098.evelyn.util.EmoticonHelper;
import com.github.nija123098.evelyn.util.Time;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IMessage;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Wraps a Discord4j {@link IMessage} object.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class Message {// should not be kept stored, too many are made
    private static final Cache<IMessage, Message> CACHE = new Cache<>(ConfigProvider.CACHE_SETTINGS.messageSize(), 30_000, Message::new);
    public static Message getMessage(String id) {
        try{
            IMessage iMessage = DiscordClient.getAny(client -> client.getMessageByID(Long.parseLong(id)));
            if (iMessage == null) return null;
            return CACHE.get(iMessage);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    public static Message getMessage(IMessage iMessage) {
        if (iMessage == null) return null;
        return CACHE.get(iMessage);
    }
    static List<Message> getMessages(List<IMessage> iMessages) {
        List<Message> messages = new ArrayList<>(iMessages.size());
        iMessages.forEach(iMessage -> messages.add(getMessage(iMessage)));
        return messages;
    }
    static List<IMessage> getIMessages(List<Message> messages) {
        List<IMessage> iMessages = new ArrayList<>(messages.size());
        messages.forEach(message -> iMessages.add(message.message()));
        return iMessages;
    }
    public static void update(IMessage iMessage) {
        Message message = CACHE.getIfPresent(iMessage);
        if (message != null) message.iMessage.set(iMessage);
    }
    private AtomicReference<IMessage> iMessage;
    private Message(IMessage message) {
        this.iMessage = new AtomicReference<>(message);
    }
    public IMessage message() {
        return this.iMessage.get();
    }

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof Message && ((Message) o).getID().equals(this.getID());
    }

    @Override
    public int hashCode() {
        return this.message().hashCode();
    }

    public String getContent() {
        return message().getContent();
    }

    public static String getCleanContent(IMessage message) {
        AtomicReference<String> reference = new AtomicReference<>(message.getContent());
        if (message.mentionsEveryone()) reference.getAndUpdate(s -> s.replace("@everyone", "***everyone***"));
        if (message.mentionsHere()) return reference.getAndUpdate(s -> s.replace("@here", "***here***"));
        message.getMentions().forEach(user -> reference.getAndUpdate(s -> s.replace(user.mention().replace("!", "!?"), "***" + user.getName() + "#" + user.getDiscriminator() + "***")));
        message.getRoleMentions().forEach(role -> reference.getAndUpdate(s -> s.replace(role.mention(), "***" + role.getName() + "***")));
        return reference.get();
    }

    public String getMentionCleanedContent() {
        return getCleanContent(this.iMessage.get());
    }

    public Channel getChannel() {
        return Channel.getChannel(message().getChannel());
    }

    public User getAuthor() {
        return User.getUser(message().getAuthor());
    }

    public long getTime() {
        return Time.toMillis(message().getTimestamp());
    }

    public List<User> getMentions() {
        return User.getUsers(message().getMentions());
    }

    public List<Role> getRoleMentions() {
        return Role.getRoles(message().getRoleMentions());
    }

    public List<Channel> getChannelMentions() {
        return Channel.getChannels(message().getChannelMentions());
    }

    public List<Attachment> getAttachments() {
        return Attachment.getAttachments(message().getAttachments());
    }

    public IMessage edit(String s) {
        return ExceptionWrapper.wrap((ExceptionWrapper.Request<IMessage>)() -> message().edit(s));
    }

    public boolean mentionsEveryone() {
        return message().mentionsEveryone();
    }

    public boolean mentionsHere() {
        return message().mentionsHere();
    }

    public void delete() {
        if (ConfigProvider.BOT_SETTINGS.ghostModeEnabled()) return;
        ExceptionWrapper.wrap(() -> message().delete());
    }

    public Optional<Instant> getEditedTimestamp() {
        return message().getEditedTimestamp();
    }

    public boolean isPinned() {
        return message().isPinned();
    }

    public Guild getGuild() {
        return Guild.getGuild(message().getGuild());
    }

    public String getFormattedContent() {
        return message().getFormattedContent();
    }

    public List<Reaction> getReactions() {
        return Reaction.getReactions(message().getReactions());
    }

    public Reaction getReaction(String unicode) {
        return Reaction.getReaction(this.message().getReactionByUnicode(unicode));
    }

    public Reaction getReactionByName(String name) {
        return getReaction(EmoticonHelper.getChars(name, false));
    }

    public Reaction addReaction(String s) {
        if (ConfigProvider.BOT_SETTINGS.ghostModeEnabled()) throw new GhostException();
        ExceptionWrapper.wrap(() -> this.message().addReaction(ReactionEmoji.of(s)));
        return getReaction(s);
    }

    public Reaction addReactionByName(String name) {
        return addReaction(EmoticonHelper.getReactionEmoji(name));
    }

    private Reaction addReaction(ReactionEmoji reactionEmoji) {
        if (ConfigProvider.BOT_SETTINGS.ghostModeEnabled()) throw new GhostException();
        try {
            ExceptionWrapper.wrap(() -> this.message().addReaction(reactionEmoji));
        } catch (PermissionsException ignored) {}// scilent falior here is acceptable
        return Reaction.getReaction(this.message().getReactionByEmoji(reactionEmoji));
    }

    public void removeReaction(Reaction reaction) {
        if (reaction == null) return;
        if (ConfigProvider.BOT_SETTINGS.ghostModeEnabled()) throw new GhostException();
        ExceptionWrapper.wrap(() -> this.message().removeReaction(DiscordClient.getOurUser().user(), reaction.reaction()));
    }

    public void removeReaction(String s) {
        if (ConfigProvider.BOT_SETTINGS.ghostModeEnabled()) throw new GhostException();
        removeReaction(getReaction(s));
    }

    public void removeReactionByName(String name) {
        if (ConfigProvider.BOT_SETTINGS.ghostModeEnabled()) throw new GhostException();
        removeReaction(getReactionByName(name));
    }

    public boolean isDeleted() {
        return message().isDeleted();
    }

    public String getID() {
        return message().getStringID();
    }

    public Shard getShard() {
        return Shard.getShard(message().getShard());
    }

    public Instant getCreationDate() {
        return message().getCreationDate();
    }
}

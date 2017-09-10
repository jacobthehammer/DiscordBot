package com.github.nija123098.evelyn.command;

import com.github.nija123098.evelyn.discordobjects.wrappers.*;
import com.github.nija123098.evelyn.template.commands.functions.ContextCommand;

/**
 * Context Command must be updated if this type's getters are changed
 *
 * @see ContextCommand
 * @author nija123098
 */
public class ContextPack {
    private User user;
    private Shard shard;
    private Channel channel;
    private Guild guild;
    private Message message;
    private Reaction reaction;
    private String args;
    public ContextPack(User user, Shard shard, Channel channel, Guild guild, Message message, Reaction reaction, String args) {
        this.user = user;
        this.shard = shard;
        this.channel = channel;
        this.guild = guild;
        this.message = message;
        this.reaction = reaction;
        this.args = args;
    }
    public User getUser() {
        return this.user;
    }
    public Shard getShard() {
        return this.shard;
    }
    public Channel getChannel() {
        return this.channel;
    }
    public Guild getGuild() {
        return this.guild;
    }
    public Message getMessage() {
        return this.message;
    }
    public Reaction getReaction() {
        return this.reaction;
    }
    public String getArgs(){
        return this.args;
    }
}

package com.github.kaaz.discordbot.config;

import com.github.kaaz.discordbot.perms.BotRole;

/**
 * Made by nija123098 on 2/20/2017.
 */
public class AbstractConfig<E> {
    private E defaul;
    private String name, description;
    private BotRole botRole;
    private ConfigLevel configLevel;
    public AbstractConfig(String name, BotRole botRole, E defaul, String description) {
        this.name = name;
        this.botRole = botRole;
        this.defaul = defaul;
        this.description = description;
    }
    public String getName() {
        return this.name;
    }
    public String getDescription() {
        return this.description;
    }
    public BotRole requiredBotRole() {
        return this.botRole;
    }
    public E getDefault(){
        return this.defaul;
    }
    void setConfigLevel(ConfigLevel level){
        this.configLevel = level;
    }
    public ConfigLevel getConfigLevel(){
        return this.configLevel;
    }
    // TODO SQL stuff goes here, more or less
    void setValue(Configurable configurable, E value){

    }
    E getValue(Configurable configurable){
        return null;
    }
}
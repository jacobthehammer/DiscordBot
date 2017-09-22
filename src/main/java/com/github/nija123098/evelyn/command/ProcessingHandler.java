package com.github.nija123098.evelyn.command;

import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.launcher.BotConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Made by nija123098 on 5/11/2017.
 */
public class ProcessingHandler {
    private static final Map<Channel, Integer> PROCESSING_MAP = new ConcurrentHashMap<>();
    public static synchronized void startProcess(Channel channel){
        if (!BotConfig.TYPING_ENABLED || channel == null) return;
        if (PROCESSING_MAP.compute(channel, (c, integer) -> integer == null ? 1 : ++integer) == 1) channel.setTypingStatus(true);
    }
    public static synchronized void endProcess(Channel channel){
        if (!BotConfig.TYPING_ENABLED || channel == null) return;
        if (PROCESSING_MAP.getOrDefault(channel, 0) == 1){
            PROCESSING_MAP.remove(channel);
            channel.setTypingStatus(false);
        } else PROCESSING_MAP.compute(channel, (c, integer) -> integer == null ? null : --integer);
    }
    public static void swapProcess(Channel origin, Channel destination){
        if (!BotConfig.TYPING_ENABLED) return;
        if (origin.equals(destination)) return;
        startProcess(destination);
        endProcess(origin);
    }
}
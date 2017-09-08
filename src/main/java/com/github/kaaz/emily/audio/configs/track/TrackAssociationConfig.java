package com.github.kaaz.emily.audio.configs.track;

import com.github.kaaz.emily.audio.Track;
import com.github.kaaz.emily.config.AbstractConfig;
import com.github.kaaz.emily.perms.BotRole;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TrackAssociationConfig extends AbstractConfig<Map<Track, Integer>, Track>{
    private static TrackAssociationConfig config;
    public TrackAssociationConfig() {
        super("track_similarities", BotRole.SYSTEM, new ConcurrentHashMap<>(), "Tracks similarities to other tracks");
        config = this;
    }
    public static void associate(Track first, Track second){
        associateOne(first, second);
        associateOne(second, first);
    }
    private static void associateOne(Track one, Track other){
        config.alterSetting(one, trackIntegerMap -> trackIntegerMap.compute(other, (track, integer) -> integer == null ? 1 : ++integer));
    }
}

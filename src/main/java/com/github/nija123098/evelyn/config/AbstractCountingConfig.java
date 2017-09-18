package com.github.nija123098.evelyn.config;

import com.github.nija123098.evelyn.launcher.Launcher;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.service.services.ScheduleService;
import com.github.nija123098.evelyn.util.Care;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractCountingConfig<T extends Configurable> extends AbstractConfig<Integer, T> {
    private Map<T, Integer> add = new ConcurrentHashMap<>();
    private Map<T, Integer> valueBase = new ConcurrentHashMap<>();
    public AbstractCountingConfig(String name, ConfigCategory category, String description) {
        super(name, category, 0, description);
        ScheduleService.scheduleRepeat(600_000, 600_000, this::save);
        Launcher.registerShutdown(this::save);
    }
    private void save(){
        this.add.forEach((t, integer) -> {
            Care.lessSleep(10);
            super.setValue(t, super.getValue(t) + integer);
            this.add.remove(t);
            this.valueBase.remove(t);
        });
    }
    @Override
    public Integer getValue(T configurable) {
        return this.valueBase.computeIfAbsent(configurable, super::getValue) + this.add.getOrDefault(configurable, 0);
    }
    @Override
    public Integer setValue(T configurable, Integer value) {
        return this.valueBase.computeIfAbsent(configurable, super::getValue) + this.add.compute(configurable, (t, integer) -> (integer == null ? 0 : integer) + (value - this.valueBase.get(t)));
    }
    @Override
    public Integer getDefault(T t) {
        return 0;
    }
    @Override
    public boolean checkDefault() {
        return false;
    }
}

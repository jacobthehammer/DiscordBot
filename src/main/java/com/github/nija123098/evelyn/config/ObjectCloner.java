package com.github.nija123098.evelyn.config;

import com.github.nija123098.evelyn.exeption.DevelopmentException;
import com.github.nija123098.evelyn.perms.configs.specialperms.SpecialPermsContainer;
import com.github.nija123098.evelyn.util.ReflectionHelper;
import javafx.util.Pair;

import java.io.File;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

/**
 * Made by nija123098 on 5/31/2017.
 */
public class ObjectCloner {
    private static final Map<Class<?>, Function<Object, Object>> MAP = new HashMap<>();
    static {
        add(Number.class);
        add(Boolean.class);
        add(Class.class);
        add(File.class);
        add(String.class);
        add(Configurable.class);
        add(List.class, CopyOnWriteArrayList::new);
        add(Set.class, Collections::synchronizedSet);
        add(Map.class, Collections::synchronizedMap);
        add(Pair.class, or -> new Pair(or.getKey(), or.getValue()));
        add(SpecialPermsContainer.class, SpecialPermsContainer::copy);
    }
    private static <T> void add(Class<T> clazz, Function<T, T> function){
        MAP.put(clazz, (Function<Object, Object>) function);
    }
    private static <T> void add(Class<T> clazz){
        MAP.put(clazz, o -> o);
    }
    public static boolean supports(Class<?> c){
        if (c.isEnum()) return true;
        for (Class<?> clazz : ReflectionHelper.getAssignableTypes(c)) if (MAP.get(clazz) != null) return true;
        return false;
    }
    public static <I> I clone(I i){
        if (i == null) return null;
        if (i.getClass().isEnum()) return i;
        for (Class<?> clazz : ReflectionHelper.getAssignableTypes(i.getClass())) {
            Function<Object, Object> function = MAP.get(clazz);
            if (function != null) return (I) function.apply(i);
        }
        throw new DevelopmentException("Attempted to clone a object of non-supported type");
    }
}
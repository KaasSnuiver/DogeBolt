package com.kaassnuvier.dogebolt.listeners;

import com.kaassnuvier.dogebolt.DogeBoltPlugin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

@AllArgsConstructor
public abstract class BaseListener<T extends Event> implements Listener {

    @Getter private final DogeBoltPlugin plugin;

    public abstract void execute(T event);

}

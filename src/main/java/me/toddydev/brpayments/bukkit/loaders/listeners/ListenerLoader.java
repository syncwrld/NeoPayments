package me.toddydev.brpayments.bukkit.loaders.listeners;

import me.toddydev.brpayments.core.utils.classes.ClassGetter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class ListenerLoader {

    public static void load(JavaPlugin instance) {
        for (Class<?> listenerClass : ClassGetter.getClassesForPackage(instance, "me.toddydev.brpayments.bukkit.listeners")) {
            if (Listener.class.isAssignableFrom(listenerClass)) {
                try {
                    Listener listener = (Listener) listenerClass.newInstance();
                    Bukkit.getPluginManager().registerEvents(listener, instance);
                } catch (Exception e) {}
            }
        }
    }

}

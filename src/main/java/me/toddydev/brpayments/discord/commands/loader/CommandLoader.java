package me.toddydev.brpayments.discord.commands.loader;

import me.toddydev.brpayments.core.utils.classes.ClassGetter;
import me.toddydev.brpayments.discord.commands.AbstractCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandLoader {

    public static void load(JavaPlugin instance, String packageName) {
        for (Class<?> commandClass : ClassGetter.getClassesForPackage(instance, packageName)) {
            if (AbstractCommand.class.isAssignableFrom(commandClass)) {
                try {
                    AbstractCommand c = (AbstractCommand) commandClass.newInstance();
                    c.register();
                    Bukkit.getConsoleSender().sendMessage("§b[DiscordHook] Loaded command " + commandClass.getName() + ".");
                } catch (Exception e) {
                    Bukkit.getConsoleSender().sendMessage("§b[DiscordHook] Failed to load command " + commandClass.getName() + ".");
                }
            }
        }
    }

}

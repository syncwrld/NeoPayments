package me.toddydev.brpayments.discord;

import com.avaje.ebean.validation.NotNull;
import lombok.Getter;
import lombok.Setter;
import me.toddydev.brpayments.discord.commands.loader.CommandLoader;
import me.toddydev.brpayments.discord.enums.MessageChannel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Discord {

    @Getter @Setter
    private static JDA api;

    private String sellChannel;

    public Discord(JavaPlugin plugin) {
        plugin.getLogger().info("[DiscordHook] Connecting to Discord...");

        String token = plugin.getConfig().getString("discord.token");
        sellChannel = plugin.getConfig().getString("discord.channels.sell");

        try {
            JDABuilder builder = JDABuilder.createDefault(token);

            builder.enableIntents(GatewayIntent.GUILD_MEMBERS); 
            builder.setMemberCachePolicy(MemberCachePolicy.ALL);

            setApi(builder.build());
        } catch (Exception e) {
            plugin.getLogger().info("[DiscordHook] Failed to connect to Discord. Token is invalid.");
            return;
        }

        CommandLoader.load(plugin, "me.toddydev.brpayments.discord.commands.register");

    }

    public void sendEmbed(MessageChannel channel, EmbedBuilder builder) {
        try {
            getApi().getTextChannelById(channel == MessageChannel.SELL ? sellChannel : "").sendMessageEmbeds(builder.build()).queue();
        } catch (Exception e) {
            System.out.println("[DiscordHook] Failed to send message to Discord. (" + e.getLocalizedMessage() + ")");
        }
    }

}

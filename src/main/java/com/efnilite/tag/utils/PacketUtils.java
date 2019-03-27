package com.efnilite.tag.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.efnilite.tag.Manager;
import com.efnilite.tag.Tag;
import com.efnilite.tag.WrapperFileConfiguration;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.inventivetalent.glow.GlowAPI;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class PacketUtils {

    public static void sendActionBar(Player player) {
        new BukkitRunnable() {
            public void run() {
                if (Manager.getTagged() == player) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Tag.getWrapperFileConfiguration().get(WrapperFileConfiguration.Configuration.MESSAGES, "action-bars.you-it")));
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(Tag.getInstance(), 0, 40);
    }

    public static void setWarning(Player player, boolean warning) {
        if (Tag.getWrapperFileConfiguration().getConfig().getBoolean("tint")) {
            PacketContainer container = new PacketContainer(PacketType.Play.Server.WORLD_BORDER);
            WorldBorder wb = player.getWorld().getWorldBorder();

            container.getWorldBorderActions().write(0, EnumWrappers.WorldBorderAction.INITIALIZE);
            container.getIntegers().write(0, 29999984);
            container.getDoubles().write(0, player.getLocation().getX())
                    .write(1, player.getLocation().getZ());
            container.getDoubles().write(3, wb.getSize())
                    .write(2, wb.getSize());
            container.getIntegers().write(2, (int) (warning ? wb.getSize() : wb.getWarningDistance()))
                    .write(1, 0);
            container.getLongs().write(0, (long) 0);
            sendPacket(player, container);
        }
    }

    public static void setGlowing(Player player, GlowAPI.Color color, List<Player> viewers) {
        if (Tag.getWrapperFileConfiguration().getConfig().getBoolean("glowing")) {
            GlowAPI.setGlowing(player, color, viewers);
        }
    }

    public static void setGlowing(Player player, GlowAPI.Color color, Player viewer) {
        if (Tag.getWrapperFileConfiguration().getConfig().getBoolean("glowing")) {
            GlowAPI.setGlowing(player, color, viewer);
        }
    }

    private static void sendPacket(Player player, PacketContainer container) {
        try {
            Tag.getProtocolManager().sendServerPacket(player, container);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}

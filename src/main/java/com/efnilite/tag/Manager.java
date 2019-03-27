package com.efnilite.tag;

import com.efnilite.tag.utils.PacketUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.inventivetalent.glow.GlowAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Manager {

    private static List<Player> players = new ArrayList<>();
    private static Player tagged;

    public static void addPlayer(Player player) {
        players.add(player);

        player.sendMessage(Tag.getWrapperFileConfiguration().get(WrapperFileConfiguration.Configuration.MESSAGES, "messages.you-joined"));
        players.forEach(p -> p.sendMessage(Tag.getWrapperFileConfiguration().get(WrapperFileConfiguration.Configuration.MESSAGES, "messages.player-joined").replace("%player%", player.getName())));

        PacketUtils.setGlowing(tagged, GlowAPI.Color.RED, player);
        player.setGameMode(GameMode.valueOf(Tag.getWrapperFileConfiguration().getConfig().getString("gamemode.join")));
        for (Player player1 :  new ArrayList<>(players).stream().filter(p ->  !players.contains(p)).collect(Collectors.toCollection(ArrayList::new))) {
            player.hidePlayer(Tag.getInstance(), player1);
        }
        if (players.size() == 1) {
            tagged = player;
            PacketUtils.setWarning(player, true);
            player.sendMessage(Tag.getWrapperFileConfiguration().get(WrapperFileConfiguration.Configuration.MESSAGES, "messages.you-it"));

            PacketUtils.setGlowing(player, GlowAPI.Color.RED, players);
            PacketUtils.sendActionBar(player);
        }
    }

    public static void removePlayer(Player player) {
        players.remove(player);

        player.sendMessage(Tag.getWrapperFileConfiguration().get(WrapperFileConfiguration.Configuration.MESSAGES, "messages.you-left"));
        players.forEach(p -> p.sendMessage(Tag.getWrapperFileConfiguration().get(WrapperFileConfiguration.Configuration.MESSAGES, "messages.player-left").replace("%player%", player.getName())));

        PacketUtils.setWarning(player, false);
        PacketUtils.setGlowing(player, null, players);
        player.setGameMode(GameMode.valueOf(Tag.getWrapperFileConfiguration().getConfig().getString("gamemode.leave")));
        for (Player player1 : new ArrayList<>(players).stream().filter(p ->  players.contains(p)).collect(Collectors.toCollection(ArrayList::new))) {
            player.showPlayer(Tag.getInstance(), player1);
        }

        if (player == tagged) {
            if (players.size() != 0) {
                Player random = players.get(new Random().nextInt(players.size()));
                random.sendMessage(Tag.getWrapperFileConfiguration().get(WrapperFileConfiguration.Configuration.MESSAGES, "messages.you-it"));

                PacketUtils.setGlowing(random, GlowAPI.Color.RED, players);
                PacketUtils.setWarning(player, true);
                tagged = random;
            } else {
                tagged = null;
            }
        }
    }

    public static List<Player> getPlayers() {
        return players;
    }

    public static Player getTagged() {
        return tagged;
    }

    public static void setTagged(Player tagged) {
        Manager.tagged = tagged;
    }
}

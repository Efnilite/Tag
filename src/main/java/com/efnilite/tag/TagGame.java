package com.efnilite.tag;

import com.efnilite.tag.utils.PacketUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.inventivetalent.glow.GlowAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class TagGame {

    private Player tagged;
    private List<Player> players = new ArrayList<>();

    public TagGame(Player tagged) {
        this.tagged = tagged;
        this.players.add(tagged);
    }

    public void add(Player player) {
        players.add(player);

        player.sendMessage(Tag.getWrapperFileConfiguration().get(WrapperFileConfiguration.Configuration.MESSAGES, "messages.you-joined"));
        players.forEach(p -> p.sendMessage(Tag.getWrapperFileConfiguration().get(WrapperFileConfiguration.Configuration.MESSAGES, "messages.player-joined").replace("%player%", player.getName())));

        PacketUtils.setGlowing(tagged, GlowAPI.Color.RED, player);
        player.setGameMode(GameMode.valueOf(Tag.getWrapperFileConfiguration().getConfig().getString("gamemode.join")));
        for (Player player1 :  new ArrayList<>(players).stream().filter(p ->  !players.contains(p)).collect(Collectors.toCollection(ArrayList::new))) {
            player.hidePlayer(Tag.getInstance(), player1);
        }

        selectTagged();
    }

    public void remove(Player player) {
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
            PacketUtils.setWarning(player, true);

            selectTagged();
        }
    }

    public void selectTagged() {
        if (players.size() == 1) {
            tagged = players.get(0);
            PacketUtils.setWarning(tagged, true);
            tagged.sendMessage(Tag.getWrapperFileConfiguration().get(WrapperFileConfiguration.Configuration.MESSAGES, "messages.you-it"));

            PacketUtils.setGlowing(tagged, GlowAPI.Color.RED, players);
            PacketUtils.sendActionBar(tagged);
        } else if (players.size() != 0) {
            Player random = players.get(new Random().nextInt(players.size()));
            random.sendMessage(Tag.getWrapperFileConfiguration().get(WrapperFileConfiguration.Configuration.MESSAGES, "messages.you-it"));

            PacketUtils.setGlowing(random, GlowAPI.Color.RED, players);
            tagged = random;
        }
    }

    public void setTagged(Player tagged) {
        this.tagged = tagged;
    }

    public Player getTagged() {
        return tagged;
    }

    public List<Player> getPlayers() {
        return players;
    }
}

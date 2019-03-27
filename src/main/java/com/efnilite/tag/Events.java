package com.efnilite.tag;

import com.efnilite.tag.utils.PacketUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.inventivetalent.glow.GlowAPI;

public class Events implements Listener {

    // --- Command --- //

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        String command = e.getMessage().replace("/", "").split(" ")[0];
        if (command.equals("tag")) {
            e.setCancelled(true);
            if (Manager.getPlayers().contains(e.getPlayer())) {
                Manager.removePlayer(e.getPlayer());
            } else {
                Manager.addPlayer(e.getPlayer());
            }
        }
    }

    // --- Events --- //

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (Manager.getPlayers().contains(e.getPlayer())) {
            Manager.removePlayer(e.getPlayer());
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            e.setCancelled(true);
            Player entity = ((Player) e.getEntity()).getPlayer();
            Player damager = ((Player) e.getDamager()).getPlayer();

            Manager.getPlayers().forEach(p -> p.sendMessage(Tag.getWrapperFileConfiguration().get(WrapperFileConfiguration.Configuration.MESSAGES, "messages.player-tagged").replace("%it%", damager.getName()).replace("%player%", entity.getName())));

            PacketUtils.setGlowing(damager, null, Manager.getPlayers());
            PacketUtils.setGlowing(entity, GlowAPI.Color.RED, Manager.getPlayers());

            if (Manager.getPlayers().contains(damager) && Manager.getPlayers().contains(entity) && Manager.getTagged() == damager) {
                Manager.setTagged(entity);
                entity.sendMessage(Tag.getWrapperFileConfiguration().get(WrapperFileConfiguration.Configuration.MESSAGES, "messages.been-tagged"));
                damager.sendMessage(Tag.getWrapperFileConfiguration().get(WrapperFileConfiguration.Configuration.MESSAGES, "messages.you-tagged").replace("%player%", entity.getName()));

                entity.sendMessage(Tag.getWrapperFileConfiguration().get(WrapperFileConfiguration.Configuration.MESSAGES, "messages.you-it"));
                PacketUtils.setGlowing(entity, GlowAPI.Color.RED, entity);
                PacketUtils.setWarning(entity, false);
                PacketUtils.sendActionBar(entity);
            }
        }
    }

}

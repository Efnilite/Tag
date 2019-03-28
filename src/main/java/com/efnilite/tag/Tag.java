package com.efnilite.tag;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.efnilite.tag.utils.UpdateChecker;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.Reader;

public class Tag extends JavaPlugin {

    private static Plugin instance;
    private static TagGame game;
    private static ProtocolManager protocolManager;
    private static WrapperFileConfiguration wrapperFileConfiguration;

    @Override
    public void onEnable() {
        game = null;
        instance = this;
        protocolManager = ProtocolLibrary.getProtocolManager();
        wrapperFileConfiguration = new WrapperFileConfiguration(this);

        new UpdateChecker().check();

        this.getServer().getPluginManager().registerEvents(new Events(), this);
    }

    public static Plugin getInstance() {
        return instance;
    }

    public static ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    public static WrapperFileConfiguration getWrapperFileConfiguration() {
        return wrapperFileConfiguration;
    }

    public static TagGame getGame() {
        return game;
    }

    public Reader getReader(String file) {
        return getTextResource(file);
    }
}

package com.efnilite.tag;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class WrapperFileConfiguration {

    private FileConfiguration config;
    private FileConfiguration messages;

    public WrapperFileConfiguration(Tag instance) {
        instance.getDataFolder().mkdir();

        config = instance.getConfig();
        messages = YamlConfiguration.loadConfiguration(instance.getReader("messages.yml"));

        instance.saveResource("config.yml", false);
        instance.saveResource("messages.yml", false);
    }

    public String get(Configuration type, String path) {
        return ChatColor.translateAlternateColorCodes('&', type.getFile().getString(path));
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public FileConfiguration getMessages() {
        return messages;
    }

    public enum Configuration {
        CONFIG {
            @Override
            public FileConfiguration getFile() {
                return Tag.getWrapperFileConfiguration().getConfig();
            }
        },
        MESSAGES {
            @Override
            public FileConfiguration getFile() {
                return Tag.getWrapperFileConfiguration().getMessages();
            }
        };

        public abstract FileConfiguration getFile();
    }
}

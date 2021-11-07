package tev.tevuqueue;

import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import tev.tevuqueue.queue.disconnectEvent;
import tev.tevuqueue.queue.joinEvent;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


public final class Main extends Plugin {
    public static Map<String, Integer> normal_queue = new HashMap<>();
    public static Map<String, Integer> premium_queue = new HashMap<>();
    File file = new File("config.yml");
    public static Configuration configuration;
    {
        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onEnable() {
        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(
                    loadResource(this, "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        getProxy().getPluginManager().registerListener(this, new joinEvent());
        getProxy().getPluginManager().registerListener(this, new disconnectEvent());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public static File loadResource(Plugin plugin, String resource) {
        File folder = plugin.getDataFolder();
        if (!folder.exists())
            folder.mkdir();
        File resourceFile = new File(folder, resource);
        try {
            if (!resourceFile.exists()) {
                resourceFile.createNewFile();
                try (InputStream in = plugin.getResourceAsStream(resource);
                     OutputStream out = new FileOutputStream(resourceFile)) {
                    ByteStreams.copy(in, out);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resourceFile;
    }
}

package tev.tevuqueue.queue;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import tev.tevuqueue.Main;

public class disconnectEvent implements Listener {
    @EventHandler
    public void onServerDisconnect(PlayerDisconnectEvent e){
        if(e.getPlayer().getServer().getInfo() == ProxyServer.getInstance().getServerInfo(Main.configuration.getString("queue.after_queue_server"))){
            if(e.getPlayer().hasPermission(Main.configuration.getString("queue.premium_queue.permission"))){
                for (Integer i: Main.premium_queue.values()) {
                    Main.normal_queue.replace(e.getPlayer().getName(), i-1);
                }
            }else{
                for (Integer i: Main.normal_queue.values()) {
                    Main.normal_queue.replace(e.getPlayer().getName(), i-1);
                }
            }
        }
        if(e.getPlayer().getServer().getInfo() == ProxyServer.getInstance().getServerInfo(Main.configuration.getString("queue.queue_server"))){
            if(e.getPlayer().hasPermission(Main.configuration.getString("queue.premium_queue.permission"))){
                Main.premium_queue.remove(e.getPlayer().getName());
                for (Integer i: Main.premium_queue.values()) {
                    Main.normal_queue.replace(e.getPlayer().getName(), i-1);
                }
            }else{
                for (Integer i: Main.normal_queue.values()) {
                    Main.normal_queue.remove(e.getPlayer().getName());
                    Main.normal_queue.replace(e.getPlayer().getName(), i-1);
                }
            }
        }
    }
}

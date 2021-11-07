package tev.tevuqueue.queue;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import tev.tevuqueue.Main;

import java.util.concurrent.TimeUnit;

public class joinEvent implements Listener {
    @EventHandler
    public void afterLogin(PostLoginEvent e){
        //This will send player to queue server.
        ServerInfo target = ProxyServer.getInstance().getServerInfo(Main.configuration.getString("queue.queue_server"));
        ServerInfo server = ProxyServer.getInstance().getServerInfo(Main.configuration.getString("queue.after_queue_server"));
        e.getPlayer().connect(target);
        //This handles queue login
        //Normal queue
        ProxiedPlayer p = e.getPlayer();
        if(p.hasPermission(Main.configuration.getString("queue.premium_queue.permission"))){
            Main.premium_queue.put(p.getName(), getHightestPlayerInNormalQueue()+1);
        }else{
            Main.normal_queue.put(p.getName(), getHightestPlayerInNormalQueue()+1);
        }

        ProxyServer.getInstance().getScheduler().schedule(ProxyServer.getInstance().getPluginManager().getPlugin("TevuQueue"), new Runnable() {
            @Override
            public void run() {
                if(Main.configuration.getBoolean("queue.premium_queue.enabled"))
                if(p.hasPermission(Main.configuration.getString("queue.premium_queue.permission"))){
                    if(Main.premium_queue.get(e.getPlayer().getName())==0){
                        p.connect(server);
                        p.sendMessage(ChatColor.GREEN+"Your queue is finished!");
                        Main.premium_queue.remove(p.getName());
                    }else{
                        p.sendMessage(ChatColor.RED + "Your queue position is "+Main.premium_queue.get(p.getName()));
                    }
                }else{
                    if(Main.normal_queue.get(e.getPlayer().getName())==0){
                        p.connect(server);
                        p.sendMessage(ChatColor.GREEN+"Your queue is finished!");
                        Main.normal_queue.remove(p.getName());
                    }else{
                        p.sendMessage(ChatColor.RED + "Your queue position is "+Main.normal_queue.get(p.getName()));
                    }
                }
                System.out.println(ChatColor.GREEN + "normal: "+ Main.normal_queue);
                System.out.println(ChatColor.GOLD + "premium: "+ Main.premium_queue);
            }
        }, 1, 2, TimeUnit.SECONDS);
    }
    public Integer getHightestPlayerInNormalQueue() {
        int pos = 0;
        for (Integer i: Main.normal_queue.values()) {
            if(i==0){
                pos = i;
            }else if(i>0){
                pos = i;
            }
        }
        return pos;
    }
}

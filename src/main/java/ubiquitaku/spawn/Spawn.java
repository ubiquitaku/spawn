package ubiquitaku.spawn;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class Spawn extends JavaPlugin implements Listener {
    FileConfiguration config;
    boolean s;
    int x;
    int y;
    int z;
    String w;
    Location l;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        config = getConfig();
        x = config.getInt("x",0);
        y = config.getInt("y",70);
        z = config.getInt("z",0);
        w = config.getString("w","world");
        s = config.getBoolean("s",false);
        saveConfig();
        l = new Location(getServer().getWorld(w),x,y,z);
        System.out.println("座標の情報が読み取れなかった場合に0,70,0設定されます\non/offの情報が読み取れなかった場合falseに設定されます\nワールド名が読み取れなかった場合worldに設定されます");
        getCommand("sspawn").setExecutor(this);
        getServer().getPluginManager().registerEvents(this,this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("sspawn")) {
            if (!(sender.isOp())) {
                sender.sendMessage("§c§lあなたにはこのこまんどを実行する権限がありません");
                return true;
            }
            if (args.length != 1) {
                sender.sendMessage("/sspawn set : 実行しているプレイヤーが立っている場所をスポーン地点に設定します(consoleからの実行は不可能)");
                sender.sendMessage("/sspawn <x> <y> <z> : プレイヤーがログイン時にスポーンする座標を設定します");
                sender.sendMessage("/sspawn reload : 設定値をリロードします");
                return true;
            }
            if (args[0].equals("on")) {
                if (s == false) {
                    config.set("s", true);
                    saveConfig();
                    s = true;
                    sender.sendMessage("serverspawnを有効化しました");
                    return true;
                }
                sender.sendMessage("onになっています");
                return true;
            }
            if (args[0].equals("off")) {
                if (s == true) {
                    config.set("s", false);
                    saveConfig();
                    s = false;
                    sender.sendMessage("serverspawnを無効化しました");
                    return true;
                }
                sender.sendMessage("offになっています");
                return true;
            }
            if (args[0].equals("reload")) {
                reloadConfig();
                sender.sendMessage("リロード完了");
                return true;
            }
            if (args[0].equals("set")) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    config.set("x", p.getLocation().getX());
                    config.set("y", p.getLocation().getY());
                    config.set("z", p.getLocation().getZ());
                    config.set("w",p.getLocation().getWorld().getName());
                    saveConfig();
                    l =  p.getLocation();
                    sender.sendMessage("座標を保存しました");
                    return true;
                }
                sender.sendMessage("consoleからの実行はできません");
                return true;
            }
            return true;
        }
        return true;
    }

    @EventHandler
    public void spawn(PlayerJoinEvent e) {
        if (s == true) {
            Player p = e.getPlayer();
            p.teleport(l);
            return;
        }
    }
}

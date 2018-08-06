package jp.msfblue1.blockcoins.Util;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by msfblue1 on 2018/06/13.
 */
public class Bank {
    public static Bank SELF;
    public Map<String,Integer> blocks = new HashMap<>();
    File database;

    public Bank() throws IOException {
        File databases = new File("plugins/BlockCoins");
        if(!databases.exists()){
            databases.mkdirs();
        }
        database = new File(databases,"database.yml");
        if(!database.exists()){
            database.createNewFile();
        }
        YamlConfiguration datas = YamlConfiguration.loadConfiguration(database);
        datas.getKeys(false).forEach(d->blocks.put(d,datas.getInt(d)));
        SELF = this;
    }

    public synchronized Integer getCoin(String uuid){
        return blocks.get(uuid) == null ? 0 : blocks.get(uuid);
    }

    public synchronized void addCoin(String uuid, Integer block){
        blocks.put(uuid, getCoin(uuid)+block);
    }

    public synchronized void addCoin(Player player, Integer block, boolean notify){
        blocks.put(player.getUniqueId().toString(), getCoin(player.getUniqueId().toString())+block);
        if(!notify){
            return;
        }
        BaseComponent component = new TextComponent(ChatColor.AQUA +String.valueOf(getCoin(player.getUniqueId().toString()))+ "Coin");
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
    }

    public synchronized boolean canRemoveCoin(String uuid, Integer block){
        return !(blocks.get(uuid) == null || blocks.get(uuid) - block < 0);
    }

    public synchronized boolean removeCoin(String uuid, Integer block){
        if(canRemoveCoin(uuid,block)){
            blocks.put(uuid, getCoin(uuid)-block);
            return true;
        }
        return false;
    }

    public synchronized boolean removeCoin(Player player, Integer block,boolean notify){
        boolean flag = removeCoin(player.getUniqueId().toString(),block);
        if(notify){
            BaseComponent component = new TextComponent(ChatColor.AQUA +String.valueOf(getCoin(player.getUniqueId().toString()))+ "Coin");
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
        }
        return flag;
    }

    public synchronized void setCoin(String uuid, Integer block){
        blocks.put(uuid,block);
    }

    public void save() throws IOException {
        if(database == null){
            throw new IOException("データベースを生成できていません!");
        }
        YamlConfiguration data = new YamlConfiguration();
        blocks.forEach(data::set);
        data.save(database);
    }
}

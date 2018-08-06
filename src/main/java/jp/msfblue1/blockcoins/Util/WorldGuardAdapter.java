package jp.msfblue1.blockcoins.Util;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.RegionQuery;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import jp.msfblue1.blockcoins.BlockCoins;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by msfblue1 on 2018/06/17.
 */
public class WorldGuardAdapter {

    private WorldGuardPlugin wg;
    private RegionContainer container;
    private RegionQuery query;

    public WorldGuardAdapter(){
        if(Bukkit.getPluginManager().getPlugin("WorldGuard") == null){
            Bukkit.getConsoleSender().sendMessage(BlockCoins.plguin.RANGENAME+ ChatColor.RED+"WorldGuardと連携していません!");
            return;
        }
        this.wg = WorldGuardPlugin.inst();
        this.container = WorldGuardPlugin.inst().getRegionContainer();
        this.query = this.container.createQuery();
    }

    public WorldGuardPlugin getWG(){
        return wg;
    }

    public RegionContainer getContainer() {
        return container;
    }

    public RegionQuery getQuery() {
        return query;
    }

    public boolean isOnlineWG(){
        return container != null;
    }

    public LocalPlayer getLP(Player p){
        return wg.wrapPlayer(p);
    }
}

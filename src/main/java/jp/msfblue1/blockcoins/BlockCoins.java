package jp.msfblue1.blockcoins;

import jp.msfblue1.blockcoins.Commands.CommandManager;
import jp.msfblue1.blockcoins.Commands.CommandNightVision;
import jp.msfblue1.blockcoins.Commands.CommandSuperPicaxe;
import jp.msfblue1.blockcoins.Listener.SPicaxeEvent;
import jp.msfblue1.blockcoins.Util.Bank;
import jp.msfblue1.blockcoins.Util.WorldGuardAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.*;

public final class BlockCoins extends JavaPlugin {

    public Bank moneymanager;
    public final String NAME = ChatColor.GOLD+"["+ChatColor.AQUA+"BlockCoins"+ChatColor.GOLD+"] "+ChatColor.RESET;
    public final String NVNAME = ChatColor.GOLD+"["+ChatColor.AQUA+"NightVision"+ChatColor.GOLD+"] "+ChatColor.RESET;
    public final String RANGENAME = ChatColor.GOLD+"["+ChatColor.AQUA+"RangeMine"+ChatColor.GOLD+"] "+ChatColor.RESET;
    public static BlockCoins plguin;
    public final List<CommandSender> picaxeuser = new LinkedList<>();
    public final List<CommandSender> modeuser = new LinkedList<>();
    //public Map<Player,Inventory> invmap = new HashMap<>();
    public List<Inventory> invs = new ArrayList<>();
    public WorldGuardAdapter wg;

    @Override
    public void onEnable() {
        plguin = this;
        try {
            moneymanager = new Bank();
            Bukkit.getPluginCommand("rangemine").setExecutor(new CommandSuperPicaxe());
            Bukkit.getPluginCommand("blockcoins").setExecutor(new CommandManager());
            new CommandNightVision();
            //Bukkit.getPluginCommand("nv").setExecutor(new CommandNightVision());
            Bukkit.getPluginManager().registerEvents(new SPicaxeEvent(),this);
            this.wg = new WorldGuardAdapter();

        } catch (IOException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(NAME+"エラー: 技術者に上のメッセージを伝えてください");
        }
    }

    @Override
    public void onDisable() {
        try {
            moneymanager.save();
            invs.forEach(d->d.getViewers().forEach(p->p.closeInventory()));
            //invmap.forEach((key, value) -> value.getViewers().forEach(HumanEntity::closeInventory));
        } catch (IOException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(NAME+"エラー: 技術者に上のメッセージを伝えてください");
            Bukkit.getConsoleSender().sendMessage(NAME+"エラー: データベースをセーブできませんでした");
        } catch (Exception ignored){

        }
    }
}

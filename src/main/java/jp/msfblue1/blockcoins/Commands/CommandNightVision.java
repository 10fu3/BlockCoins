package jp.msfblue1.blockcoins.Commands;

import jp.msfblue1.blockcoins.BlockCoins;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by msfblue1 on 2018/06/18.
 */
public class CommandNightVision implements CommandExecutor,Listener {
    public CommandNightVision(){
        Bukkit.getPluginManager().registerEvents(this,BlockCoins.plguin);
        Bukkit.getPluginCommand("nv").setExecutor(this);
        Bukkit.getOnlinePlayers().forEach(CommandNightVision::execute);
    }

    @EventHandler
    public void onSpawn(PlayerRespawnEvent e){
        e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1, false, false));
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1, false, false));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return execute(sender);
    }

    public static boolean execute(CommandSender sender){
        if (sender instanceof Player) {

            if(((Player) sender).hasPotionEffect(PotionEffectType.NIGHT_VISION)){
                ((Player) sender).removePotionEffect(PotionEffectType.NIGHT_VISION);
                sender.sendMessage(BlockCoins.plguin.NVNAME+ ChatColor.YELLOW+"NightVisionを無効にしました");
            }else{
                ((Player) sender).addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1, false, false));
                sender.sendMessage(BlockCoins.plguin.NVNAME+ ChatColor.YELLOW+"NightVisionを有効にしました");
            }
            return true;
        }else{
            sender.sendMessage(BlockCoins.plguin.NVNAME+ ChatColor.RED+"ゲーム内から実行してください");
            return true;
        }
    }
}

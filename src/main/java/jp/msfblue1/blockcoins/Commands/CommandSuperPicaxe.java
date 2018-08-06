package jp.msfblue1.blockcoins.Commands;

import jp.msfblue1.blockcoins.BlockCoins;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by msfblue1 on 2018/06/13.
 */
public class CommandSuperPicaxe implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(BlockCoins.plguin+"エラー :ゲーム内から実行してください");
            return true;
        }
        List<CommandSender> targets = BlockCoins.plguin.picaxeuser;
        String NAME = BlockCoins.plguin.RANGENAME;
        boolean flag = false;
        if(args.length > 0){
            switch (args[0]){
                case "on":
                    if(!targets.contains(sender)){
                        targets.add(sender);
                        flag = true;
                    }
                    if(!BlockCoins.plguin.modeuser.contains(sender)){
                        sender.sendMessage(NAME+ ChatColor.YELLOW+"RangeMineを機能させるにはBlockCoinsを有効にする必要があります");
                        sender.sendMessage(NAME+ChatColor.AQUA+"次のコマンドで有効にできます: /blockcoins on");
                    }
                    sender.sendMessage(NAME + (flag ? "有効にしました":"すでに有効です"));
                    return true;
                case "off":
                    if(targets.contains(sender)){
                        targets.remove(sender);
                        flag = true;
                    }
                    sender.sendMessage(NAME + (flag ? "無効にしました":"すでに無効です"));
                    return true;
                default:
                    sender.sendMessage("ヘルプ - /rangemine on");
                    sender.sendMessage("ヘルプ - /rangemine off");
            }

        }else{
            if(targets.contains(sender)){
                targets.remove(sender);
            }else{
                targets.add(sender);
                if(!BlockCoins.plguin.modeuser.contains(sender)){
                    sender.sendMessage(NAME+ ChatColor.YELLOW+"RangeMineを機能させるにはBlockCoinsを有効にする必要があります");
                    sender.sendMessage(NAME+ChatColor.AQUA+"次のコマンドで有効にできます: /blockcoins on");
                }
            }
            sender.sendMessage(NAME+(targets.contains(sender) ? "有効にしました":"無効にしました"));
        }
        return true;
    }
}

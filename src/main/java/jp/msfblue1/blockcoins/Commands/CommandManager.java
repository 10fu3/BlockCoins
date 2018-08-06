package jp.msfblue1.blockcoins.Commands;

import jp.msfblue1.blockcoins.Util.Bank;
import jp.msfblue1.blockcoins.BlockCoins;
import jp.msfblue1.blockcoins.Util.GuiManager;
import jp.msfblue1.blockcoins.Util.ItemInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by msfblue1 on 2018/06/13.
 */
public class CommandManager implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean isPlayer = sender instanceof Player;

        if (args.length > 0) {
            switch (args[0]) {
                case "about":{
                    if (sender.hasPermission("blockcoins.admin")) {
                        if (args.length == 2) {
                            Bukkit.getScheduler().runTaskAsynchronously(BlockCoins.plguin, () -> {
                                OfflinePlayer p = Bukkit.getOfflinePlayer(args[1]);
                                if (p == null || p.getName() == null) {
                                    sender.sendMessage(BlockCoins.plguin.NAME + "エラー - プレーヤーが見つりません!");
                                    return;
                                }
                                Integer coin = BlockCoins.plguin.moneymanager.getCoin(p.getUniqueId().toString());
                                sender.sendMessage(MessageFormat.format("{0}{1}は{2}Coin持っています", BlockCoins.plguin.NAME, p.getName(), String.valueOf(coin)));
                            });
                        } else {
                            sender.sendMessage(BlockCoins.plguin.NAME + "ヘルプ - /blockcoins about プレーヤー名");
                        }
                    }
                    break;
                }
                case "on":{
                    sender.sendMessage(BlockCoins.plguin.NAME + (BlockCoins.plguin.modeuser.contains(sender) ? "すでに有効です" : "有効にしました"));
                    BlockCoins.plguin.modeuser.add(sender);
                    break;
                }
                case "off":{
                    sender.sendMessage(BlockCoins.plguin.NAME + (!BlockCoins.plguin.modeuser.contains(sender) ? "すでに無効です" : "無効にしました"));
                    BlockCoins.plguin.modeuser.remove(sender);
                    break;
                }
                case "toggle":{
                    if (BlockCoins.plguin.modeuser.contains(sender)) {
                        sender.sendMessage(BlockCoins.plguin.NAME + "無効にしました");
                        BlockCoins.plguin.modeuser.remove(sender);
                    } else {
                        sender.sendMessage(BlockCoins.plguin.NAME + "有効にしました");
                        BlockCoins.plguin.modeuser.add(sender);
                    }
                    break;
                }
                case "add": {
                    if (sender.hasPermission("blockcoins.admin")) {
                        Bukkit.getScheduler().runTaskAsynchronously(BlockCoins.plguin, () -> {
                            try {
                                if (args.length < 2) {
                                    sender.sendMessage(BlockCoins.plguin.NAME + "ヘルプ - /blockcoins add プレーヤー名 追加個数");
                                    return;
                                }
                                OfflinePlayer target = Bukkit.getPlayer(args[1]);
                                if (target == null || target.getName() == null) {
                                    sender.sendMessage(BlockCoins.plguin.NAME + "ヘルプ - 対象のプレーヤーは存在しません");
                                    return;
                                }
                                Bank.SELF.addCoin(target.getUniqueId().toString(), Integer.valueOf(args[2]));
                                Integer coin = BlockCoins.plguin.moneymanager.getCoin(target.getUniqueId().toString());
                                sender.sendMessage(MessageFormat.format("{0}{1}の所持金を{2}Coinにしました", BlockCoins.plguin.NAME, target.getName(), String.valueOf(coin)));
                            } catch (NumberFormatException e) {
                                sender.sendMessage(BlockCoins.plguin.NAME + "ヘルプ - /blockcoins add プレーヤー名 追加個数");
                            }
                        });
                    }
                    break;
                }
                case "set":{
                    if (sender.hasPermission("blockcoins.admin")) {
                        Bukkit.getScheduler().runTaskAsynchronously(BlockCoins.plguin, () -> {
                            try {
                                if (args.length < 2) {
                                    sender.sendMessage(BlockCoins.plguin.NAME + "ヘルプ - /blockcoins set プレーヤー名 追加個数");
                                    return;
                                }
                                OfflinePlayer target = Bukkit.getPlayer(args[1]);
                                if (target == null || target.getName() == null) {
                                    sender.sendMessage(BlockCoins.plguin.NAME + "ヘルプ - 対象のプレーヤーは存在しません");
                                    return;
                                }
                                Bank.SELF.setCoin(target.getUniqueId().toString(), Integer.valueOf(args[2]));
                                Integer coin = BlockCoins.plguin.moneymanager.getCoin(target.getUniqueId().toString());
                                sender.sendMessage(MessageFormat.format("{0}{1}の所持金を{2}Coinにしました", BlockCoins.plguin.NAME, target.getName(), String.valueOf(coin)));
                            } catch (NumberFormatException e) {
                                sender.sendMessage(BlockCoins.plguin.NAME + "ヘルプ - /blockcoins set プレーヤー名 追加個数");
                            }
                        });
                    }
                    break;
                }
                case "remove":{
                    if (sender.hasPermission("blockcoins.admin")) {
                        Bukkit.getScheduler().runTaskAsynchronously(BlockCoins.plguin, () -> {
                            try {
                                if (args.length < 2) {
                                    sender.sendMessage(BlockCoins.plguin.NAME + "ヘルプ - /blockcoins remove プレーヤー名 追加個数");
                                    return;
                                }
                                OfflinePlayer target = Bukkit.getPlayer(args[1]);
                                if (target == null) {
                                    sender.sendMessage(BlockCoins.plguin.NAME + "ヘルプ - 対象のプレーヤーがオフラインです");
                                    return;
                                }
                                if (!Bank.SELF.canRemoveCoin(target.getUniqueId().toString(), Integer.valueOf(args[2]))) {
                                    sender.sendMessage(BlockCoins.plguin.NAME + "ヘルプ - 対象のプレーヤーの残高不足です");
                                    return;
                                }
                                Bank.SELF.removeCoin(target.getUniqueId().toString(), Integer.valueOf(args[2]));
                                Integer coin = BlockCoins.plguin.moneymanager.getCoin(target.getUniqueId().toString());
                                sender.sendMessage(MessageFormat.format("{0}{1}の所持金を{2}Coinにしました", BlockCoins.plguin.NAME, target.getName(), String.valueOf(coin)));
                            } catch (NumberFormatException e) {
                                sender.sendMessage(BlockCoins.plguin.NAME + "ヘルプ - /blockcoins remove プレーヤー名 追加個数");
                            }
                        });
                    }
                    break;
                }
                case "sell":{
                    if (!isPlayer) {
                        sender.sendMessage(BlockCoins.plguin.NAME + "エラー - ゲーム内から実行してください");
                        return true;
                    } else {
                        Listener[] l = new Listener[1];
                        Inventory inv = GuiManager.createGUI(GuiManager.GUIMODE.SELL);
                        l[0] = new Listener() {
                            @EventHandler
                            public void onClick(InventoryClickEvent e) {
                                if (e.getWhoClicked().equals(sender)) {
                                    switch (e.getClick()){
                                        case SHIFT_LEFT:
                                        case SHIFT_RIGHT:
                                            Bukkit.getWorld(e.getWhoClicked().getLocation().getWorld().getUID())
                                                    .playSound(e.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BASS, 1, 2);
                                            e.setCancelled(true);
                                            return;
                                    }
                                    int index = e.getRawSlot();
                                    //ItemStack button = e.getClickedInventory().getItem(e.getRawSlot());
                                    //List<ItemStack> buttons = Arrays.asList(GuiManager.getItems());
                                    if (GuiManager.getItems().length >= index + 1 && index >= 0) {
                                        e.setCancelled(true);
                                        ItemStack button = GuiManager.getItems()[index];
                                        final int[] transactioncount = new int[1];
                                        switch (e.getClick()) {
                                            case RIGHT:
                                                transactioncount[0] = 64;
                                                break;
                                            default:
                                                transactioncount[0] = 1;
                                                break;
                                        }

                                        //int slotid = buttons.indexOf(button);
                                        Optional.ofNullable(e.getWhoClicked().getInventory()).ifPresent(Pinv -> {

                                            Map<Integer, ? extends ItemStack> slots =
                                                    Pinv.all(button.getType())
                                                            .entrySet()
                                                            .stream()
                                                            .filter(d->GuiManager.equals(d.getValue(),button))
//                                                            .filter(d -> GuiManager.convertID(d.getValue()) == button.getDurability())
//                                                            .filter(d -> d.getValue().getItemMeta().equals(button.getItemMeta()))
                                                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                                            Integer targetscount = slots.values().stream().mapToInt(ItemStack::getAmount).sum();

                                            //Bukkit.getConsoleSender().sendMessage(String.valueOf(targetscount)+" "+button.getType()+" "+button.getDurability());

                                            if (targetscount <= 0) {
                                                e.getWhoClicked().sendMessage(BlockCoins.plguin.NAME + "エラー - 売買に必要なアイテムが足りません！");
                                                Bukkit.getWorld(e.getWhoClicked().getLocation().getWorld().getUID())
                                                        .playSound(e.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BASS, 1, 2);
                                                return;
                                            }
                                            if (targetscount < transactioncount[0]) {
                                                transactioncount[0] = targetscount;
                                            }

                                            Bank.SELF.addCoin(((Player) e.getWhoClicked()), transactioncount[0], true);
                                            int tc = transactioncount[0];

                                            for (ItemStack stack : slots.values()) {
                                                if (tc >= 0) {
                                                    if (stack.getAmount() - tc >= 0) {
                                                        stack.setAmount(stack.getAmount() - tc);
                                                        tc = 0;
                                                    } else if (stack.getAmount() - tc < 0) {
                                                        tc = tc - stack.getAmount();
                                                        stack.setAmount(0);
                                                    }
                                                }
                                            }

                                            Bukkit.getWorld(e.getWhoClicked().getLocation().getWorld().getUID())
                                                    .playSound(e.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_XYLOPHONE, 1, 5);

                                        });
                                    }

                                }
                            }

                            @EventHandler
                            public void onClose(InventoryCloseEvent e) {
                                if (inv.equals(e.getInventory()) && e.getPlayer().equals(sender)) {
                                    HandlerList.unregisterAll(l[0]);
                                    //BlockCoins.plguin.invmap.remove(e.getPlayer());
                                }
                            }

                            @EventHandler
                            public void onExit(PlayerQuitEvent e) {
                                if (e.getPlayer().equals(sender)) {
                                    HandlerList.unregisterAll(l[0]);
                                    //BlockCoins.plguin.invmap.remove(e.getPlayer());
                                }
                            }

                        };
                        //BlockCoins.plguin.invmap.put(((Player)sender),inv);
                        BlockCoins.plguin.invs.add(inv);
                        ((Player) sender).openInventory(inv);
                        Bukkit.getPluginManager().registerEvents(l[0], BlockCoins.plguin);
                    }
                    break;
                }
                case "buy":{
                    if (!isPlayer) {
                        sender.sendMessage(BlockCoins.plguin.NAME + "エラー - ゲーム内から実行してください");
                        return true;
                    } else {
                        Listener[] l = new Listener[1];
                        Inventory inv = GuiManager.createGUI(GuiManager.GUIMODE.BUY);
                        l[0] = new Listener() {
                            @EventHandler
                            public void onClick(InventoryClickEvent e) {
                                if (e.getWhoClicked().equals(sender)) {
                                    switch (e.getClick()){
                                        case SHIFT_LEFT:
                                        case SHIFT_RIGHT:
                                            Bukkit.getWorld(e.getWhoClicked().getLocation().getWorld().getUID())
                                                    .playSound(e.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BASS, 1, 2);
                                            e.setCancelled(true);
                                            return;
                                    }
                                    int index = e.getRawSlot();
                                    //List<ItemStack> buttons = Arrays.asList(GuiManager.getItems());
                                    if (GuiManager.getItems().length >= index + 1 && index >= 0) {
                                        e.setCancelled(true);
                                        ItemStack button = GuiManager.getItems()[index];
                                        int transactioncount;
                                        switch (e.getClick()) {
                                            case RIGHT:
                                                transactioncount = 64;
                                                break;
                                            default:
                                                transactioncount = 1;
                                                break;
                                        }
                                        if (Bank.SELF.getCoin(e.getWhoClicked().getUniqueId().toString()) - transactioncount >= 0) {
                                            Bank.SELF.removeCoin((Player) e.getWhoClicked(), transactioncount, true);
                                            ItemStack addstacks = new ItemInfo(button.getType(), button.getDurability())
                                                    .setAmmount(transactioncount)
                                                    .setItemMeta(d -> d.setLore(new ArrayList<>()))
                                                    .toStack();
                                            Integer surplus =
                                                    e.getWhoClicked().getInventory().addItem(addstacks)
                                                            .values()
                                                            .stream()
                                                            .mapToInt(ItemStack::getAmount)
                                                            .sum();
                                            Bukkit.getWorld(e.getWhoClicked().getLocation().getWorld().getUID())
                                                    .playSound(e.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_XYLOPHONE, 1, 5);
                                            if (surplus > 0) {
                                                Bank.SELF.addCoin((Player) e.getWhoClicked(), surplus, true);
                                                e.getWhoClicked().sendMessage(ChatColor.GOLD + "格納できない分を払い戻ししました");
                                                Bukkit.getWorld(e.getWhoClicked().getLocation().getWorld().getUID())
                                                        .playSound(e.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_HARP, 1, 1);
                                            }
                                        } else {
                                            e.getWhoClicked().sendMessage(BlockCoins.plguin.NAME + "エラー - 所持コインが足りません！");
                                            Bukkit.getWorld(e.getWhoClicked().getLocation().getWorld().getUID())
                                                    .playSound(e.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BASS, 1, 2);
                                        }
                                    }

                                }
                            }

                            @EventHandler
                            public void onClose(InventoryCloseEvent e) {
                                if (inv.equals(e.getInventory()) && e.getPlayer().equals(sender)) {
                                    HandlerList.unregisterAll(l[0]);
                                    //BlockCoins.plguin.invmap.remove(e.getPlayer());
                                }
                            }

                            @EventHandler
                            public void onExit(PlayerQuitEvent e) {
                                if (e.getPlayer().equals(sender)) {
                                    HandlerList.unregisterAll(l[0]);
                                    //BlockCoins.plguin.invmap.remove(e.getPlayer());
                                }
                            }
                        };
                        Bukkit.getPluginManager().registerEvents(l[0], BlockCoins.plguin);
                        BlockCoins.plguin.invs.add(inv);
                        ((Player) sender).openInventory(inv);

                    }
                    break;
                }
                default:{
                    sender.sendMessage(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', "&6=============[] BLOCK COINS []============="));
                    sender.sendMessage(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', "&f/blockcoins help &7//ヘルプを表示します"));
                    sender.sendMessage(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', "&f/blockcoins on   &7//コインの自動変換をONにします"));
                    sender.sendMessage(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', "&f/blockcoins off  &7//コインの自動変換をOFFにします"));
                    sender.sendMessage(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', "&f/blockcoins toggle  &7//コインの自動変換を切り替えします"));
                    if (sender.hasPermission("blockcoins.admin")) {
                        sender.sendMessage(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', "&f/blockcoins set プレーヤー名 追加個数 &7//コインをセットします"));
                        sender.sendMessage(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', "&f/blockcoins add プレーヤー名 追加個数 &7//コインを追加します"));
                        sender.sendMessage(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', "&f/blockcoins remove プレーヤー名 追加個数 &7//コインを徴収します"));
                        sender.sendMessage(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', "&f/blockcoins about プレーヤー名 &7//対象のプレーヤーのコイン保有数を表示します"));
                    }
                    break;
                }
            }
        } else if (args.length == 0) {
            sender.sendMessage(
                    BlockCoins.plguin.NAME + (
                            isPlayer
                                    ?
                                    "あなたは" + String.valueOf(Bank.SELF.getCoin(((Player) sender).getUniqueId().toString())) + "Coinを所持しています"
                                    :
                                    ChatColor.RED + "ゲーム内から実行してください"));
        }
        return true;
    }
}

package jp.msfblue1.blockcoins.Util;

import jp.msfblue1.blockcoins.Util.ItemInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * Created by msfblue1 on 2018/06/13.
 */
public class GuiManager {
    public static ItemStack[] getItems(){
        return new ItemStack[]{
             createItem(Material.COBBLESTONE, 0),
             createItem(Material.STONE, 0),
             createItem(Material.STONE,1),
             createItem(Material.STONE,3),
             createItem(Material.STONE,5),
             createItem(Material.SAND,0),
             createItem(Material.SAND,1),
             createItem(Material.DIRT,0),
             createItem(Material.DIRT,1),
             createItem(Material.GRAVEL, 1),
             createItem(Material.SANDSTONE, 0),
             createItem(Material.SANDSTONE, 1),
             createItem(Material.NETHERRACK, 1),
             createItem(Material.ENDER_STONE, 1),
             createItem(Material.GRASS,1),
             createItem(Material.DIRT,2)
     };
    }


    public enum GUIMODE{SELL,BUY}
    public static Inventory createGUI(GUIMODE mode){
        Inventory gui = Bukkit.createInventory(null, InventoryType.CHEST,mode == GUIMODE.BUY ? ChatColor.GREEN+"BUY":ChatColor.GOLD+"SELL");
//        gui.addItem(getItems(mode));
        gui.addItem(addExplain(mode,getItems()));
        return gui;
    }

    public static ItemStack createItem( Material material,int type){
        ItemStack stack = new ItemStack(material,1);
        stack.setDurability((short)type);
        return stack;
    }

    public static ItemStack[] addExplain(GUIMODE mode, ItemStack[] targets){
        List<ItemStack> items = Arrays.asList(targets.clone())
                .stream()
                .map(d -> new ItemInfo(d.getType(), d.getDurability()))
                .map(d -> d.setItemMeta(meta -> {
                    meta.setLore(Arrays.asList("1個 1Coin", ChatColor.GRAY + "左クリックで  1個" + (mode == GUIMODE.BUY ? "購入" : "売却"), ChatColor.GRAY + "右クリックで 64個" + (mode == GUIMODE.BUY ? "購入" : "売却")));
                    d.setItem(i->i.setItemMeta(meta));
                }))
                .map(ItemInfo::toStack)
                .collect(Collectors.toList());
        ItemStack[] stacks = new ItemStack[items.size()];
        IntStream.range(0,items.size())
                .forEach(c->stacks[c] = items.get(c));
        return stacks;
    }

    public static boolean equals(ItemStack s1, ItemStack s2){
        if(s1 == null && s2 == null){
            return true;
        }else if(s1 == null || s2 == null){
            return false;
        }else{
            if(s1.getType() == s2.getType()){
                if(convertID(s1) == convertID(s2)){
                    if(s1.getItemMeta().equals(s2.getItemMeta())){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static short convertID(ItemStack stack){
        switch (stack.getType()){
            case GRAVEL:
            case NETHERRACK:
            case ENDER_STONE:
            case GRASS:
                return 1;
            default:
                return stack.getDurability();
        }
    }

    public static ItemInfo toItemInfo(Material material, int type){
        return new ItemInfo(material,type);
    }


}

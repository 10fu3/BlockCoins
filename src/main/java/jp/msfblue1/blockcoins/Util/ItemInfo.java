package jp.msfblue1.blockcoins.Util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.function.Consumer;

/**
 * Created by msfblue1 on 2018/06/14.
 */
public class ItemInfo{
    private ItemStack stack = new ItemStack(Material.AIR);
    public ItemInfo(Material material,int type){
        setMaterial(material).setType(type);
    }

    public ItemInfo setAmmount(Integer count){
        stack.setAmount(count);
        return this;
    }

    public ItemInfo setMaterial(Material material){
        stack.setType(material);
        return this;
    }

    public ItemInfo setType(Integer id){
        stack.setDurability(id.shortValue());
        return this;
    }

    public Material getMaterial(){
        return stack.getType();
    }
    public int getType(){
        return stack.getDurability();
    }

    public ItemInfo setItemMeta(Consumer<ItemMeta> meta){
        meta.accept(stack.getItemMeta());
        return this;
    }

    public ItemInfo setItem(Consumer<ItemStack> stack){
        stack.accept(this.stack);
        return this;
    }

    public ItemStack toStack(){
        return stack;
    }
}

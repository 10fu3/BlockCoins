package jp.msfblue1.blockcoins.Listener;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.RegionQuery;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import jp.msfblue1.blockcoins.Util.Bank;
import jp.msfblue1.blockcoins.BlockCoins;
import jp.msfblue1.blockcoins.Util.WorldGuardAdapter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.stream.Collectors;

/**
 * Created by msfblue1 on 2018/06/13.
 */
public class SPicaxeEvent implements Listener{

    enum IGNORE {X,Y,Z}

    @EventHandler
    public void onTouch(PlayerInteractEvent e) {
        if(!BlockCoins.plguin.modeuser.contains(e.getPlayer())){
            return;
        }
        if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
            final Listener[] l = new Listener[1];
            l[0] = new Listener() {
                @EventHandler
                public void onBreak(BlockBreakEvent e1) {
                    if (e.getClickedBlock().getLocation().equals(e1.getBlock().getLocation())) {
                        boolean flag;
                        switch (e1.getPlayer().getInventory().getItemInMainHand().getType()){
                            case GOLD_SPADE:
                            case STONE_SPADE:
                            case IRON_SPADE:
                            case WOOD_SPADE:
                            case DIAMOND_SPADE:
                            case DIAMOND_PICKAXE:
                            case GOLD_PICKAXE:
                            case IRON_PICKAXE:
                            case STONE_PICKAXE:
                            case WOOD_PICKAXE:
                                flag = true;
                                break;
                            default:
                                flag = false;
                                break;
                        }
                        if(!flag){
                            return;
                        }
                        Location center = e1.getBlock().getLocation().clone();
                        switch (e1.getBlock().getType()) {
                            case STONE:
                            case COBBLESTONE:
                            case SANDSTONE:
                            case ENDER_STONE:
                            case NETHERRACK:
                            case GRASS:
                            case MYCEL:
                            case DIRT:
                            case GRAVEL:
                            case GRASS_PATH:
                            case SOIL:
                            case SAND:
                                WorldGuardAdapter wga = BlockCoins.plguin.wg;
                                if(!BlockCoins.plguin.picaxeuser.contains(e1.getPlayer())){
                                    if(wga.isOnlineWG()){
                                        RegionQuery query = wga.getQuery();
                                        ApplicableRegionSet regions = query.getApplicableRegions(e1.getBlock().getLocation());
                                        LocalPlayer lp = wga.getLP(e1.getPlayer());
                                        int target = regions
                                                .getRegions()
                                                .stream()
                                                .filter(r->!r.isMember(lp) && !r.isOwner(lp)).collect(Collectors.toList()).size();
                                        if(target > 0){
                                            return;
                                        }
                                    }

                                    e1.getBlock().setType(Material.AIR);
                                    Bank.SELF.addCoin(e1.getPlayer().getPlayer(),1,true);
                                    e1.setCancelled(true);
                                    return;
                                }

                                IGNORE ignore = null;

                                switch (e.getBlockFace()) {
                                    case NORTH:
                                        //ignore = IGNORE.Z;
                                        //center.setZ(center.getZ() + 1);
                                        //break;
                                    case SOUTH:
                                        ignore = IGNORE.Z;
                                        //center.setZ(center.getZ() - 1);
                                        break;
                                    case EAST:
                                        //ignore = IGNORE.X;
                                        //center.setX(center.getX() - 1);
                                        //break;
                                    case WEST:
                                        ignore = IGNORE.X;
                                        //center.setX(center.getX() + 1);
                                        break;
                                    case UP:
                                        //ignore = IGNORE.Y;
                                        //center.setY(center.getY() - 1);
                                        //break;
                                    case DOWN:
                                        ignore = IGNORE.Y;
                                        //center.setY(center.getY() + 1);
                                        break;
                                }
                                Location loc;

                                Integer howmany = 0;

                                for (int x = -1; x < 2; x++) {
                                    if(ignore == IGNORE.X && x != 0){
                                        continue;
                                    }

                                    for (int y = -1; y < 2; y++) {

                                        if(ignore == IGNORE.Y && y != 0){
                                            continue;
                                        }
                                        for (int z = -1; z < 2; z++) {
                                            if(ignore == IGNORE.Z && z != 0){
                                                continue;
                                            }
                                            loc = new Location(e1.getBlock().getWorld(), center.getX() + x, center.getY() + y, center.getZ() + z);
                                            if(wga.isOnlineWG()){
                                                RegionQuery query = wga.getQuery();
                                                ApplicableRegionSet regions = query.getApplicableRegions(loc);
                                                LocalPlayer lp = wga.getLP(e1.getPlayer());
                                                int target = regions
                                                            .getRegions()
                                                            .stream()
                                                            .filter(r->!r.isMember(lp) && !r.isOwner(lp)).collect(Collectors.toList()).size();
                                                if(target > 0){
                                                    continue;
                                                }
                                            }

                                            switch (loc.getBlock().getType()) {
                                                case STONE:
                                                case COBBLESTONE:
                                                case SANDSTONE:
                                                case ENDER_STONE:
                                                case NETHERRACK:
                                                    loc.getWorld().playSound(loc, Sound.BLOCK_STONE_PLACE, 1, 1);
                                                    loc.getBlock().setType(Material.AIR);
                                                    howmany++;
                                                    break;
                                                case GRASS:
                                                case MYCEL:
                                                case DIRT:
                                                case GRAVEL:
                                                case GRASS_PATH:
                                                case SOIL:
                                                    loc.getWorld().playSound(loc, Sound.BLOCK_GRASS_PLACE, 1, 1);
                                                    loc.getBlock().setType(Material.AIR);
                                                    howmany++;
                                                    break;
                                                case SAND:
                                                    loc.getWorld().playSound(loc, Sound.BLOCK_SAND_PLACE, 1, 1);
                                                    loc.getBlock().setType(Material.AIR);
                                                    howmany++;
                                                    break;
                                            }
                                        }
                                    }
                                }
                                Bank.SELF.addCoin(e1.getPlayer(),howmany,true);
                                e1.setCancelled(true);
                                break;
                        }
                    }
                    HandlerList.unregisterAll(l[0]);
                }
            };
            Bukkit.getPluginManager().registerEvents(l[0], BlockCoins.plguin);
        }
    }
}

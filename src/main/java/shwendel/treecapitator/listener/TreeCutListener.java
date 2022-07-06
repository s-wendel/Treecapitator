package shwendel.treecapitator.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import shwendel.treecapitator.Treecapitator;

import java.util.ArrayList;
import java.util.List;

public class TreeCutListener implements Listener {

    final BlockFace[] faces = Treecapitator.getInstance().getConfig().getBoolean("options.diagonal_logs")
            ? new BlockFace[] { BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST }
            : new BlockFace[] { BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };

    @EventHandler
    public void treeCut(BlockBreakEvent event) {

        Player player = event.getPlayer();
        String permission = Treecapitator.getInstance().getConfig().getString("options.permission_to_decapitate");

        if(!permission.equals("")) {
            if(!player.hasPermission(permission)) {
                return;
            }
        }

        if(Treecapitator.getInstance().isLog(event.getBlock())) {

            List<Block> logsLeft = new ArrayList<>();
            logsLeft.add(event.getBlock());


            new BukkitRunnable() {

                int logCounter = 0;

                @Override
                public void run() {

                    if(logsLeft.isEmpty() || logCounter > Treecapitator.getInstance().getConfig().getInt("options.max_logs")) {
                        cancel();
                        return;
                    }

                    Block log = logsLeft.get(0);

                    if(Treecapitator.getInstance().getConfig().getBoolean("options.auto_pickup_drops")) {
                        for(ItemStack item : log.getDrops(player.getInventory().getItemInMainHand())) {
                            player.getInventory().addItem(item);
                        }
                        log.setType(Material.AIR);
                    } else {
                        log.breakNaturally(player.getInventory().getItemInMainHand());
                    }

                    for(BlockFace face : faces) {
                        Block relative = log.getRelative(face);
                        if(Treecapitator.getInstance().isLog(relative) && !logsLeft.contains(relative)) {
                            logsLeft.add(relative);
                        }
                    }

                    logsLeft.remove(0);
                    logCounter++;

                }

            }.runTaskTimer(Treecapitator.getInstance(), 0L, 1L);
        }

    }


}

package shwendel.treecapitator;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;
import shwendel.treecapitator.listener.JobsTreeCutListener;
import shwendel.treecapitator.listener.TreeCutListener;

public final class Treecapitator extends JavaPlugin {

    private static Treecapitator instance;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        instance = this;


        if(getServer().getPluginManager().getPlugin("Jobs") != null) {
            getServer().getPluginManager().registerEvents(new JobsTreeCutListener(), this);
        } else {
            getServer().getPluginManager().registerEvents(new TreeCutListener(), this);
        }

    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static Treecapitator getInstance() {
        return instance;
    }

    public boolean isLog(Material material) {
        String name = material.name();
        if(getConfig().getBoolean("options.mangrove_roots") && name.equals("MANGROVE_ROOTS")) {
            return true;
        }
        return (name.endsWith("LOG") || name.equals("CRIMSON_STEM") || name.equalsIgnoreCase("WARPED_STEM")) && !name.startsWith("STRIPPED");
    }

    public boolean isLog(Block block) {
        return isLog(block.getType());
    }

}

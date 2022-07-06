package shwendel.treecapitator;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;
import shwendel.treecapitator.listener.TreeCutListener;

public final class Treecapitator extends JavaPlugin {

    private static Treecapitator instance;

    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(new TreeCutListener(), this);
        saveDefaultConfig();
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

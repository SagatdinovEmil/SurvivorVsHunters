package code;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.plugin.java.JavaPlugin;

import static org.bukkit.ChatColor.DARK_BLUE;
import static org.bukkit.enchantments.Enchantment.VANISHING_CURSE;


public class Utils {
    // Game stages
    public static int PRELUDE = 0;
    public static int HANDICAP = 1;
    public static int HUNTING = 2;
    public static int END = 3;

    public static void giveSurvivorCompass(Player p) {
        // Dont give duplicate
        for(ItemStack item : p.getInventory().getContents()){
            if (item == null) continue;
            if(item.getItemMeta().getDisplayName().equals(DARK_BLUE + "Survivor compass")){
                return;
            }
        }
        ItemStack compass = new ItemStack(Material.COMPASS);
        CompassMeta meta = (CompassMeta) compass.getItemMeta();
        meta.setDisplayName(DARK_BLUE + "Survivor compass");
        meta.addEnchant(VANISHING_CURSE,1, false);
        compass.setItemMeta(meta);
        p.getInventory().setItemInMainHand(compass);
    }

    // Send message to all players
    public static void say(JavaPlugin plugin, String msg) {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            player.sendMessage(msg);
        }
    }
}

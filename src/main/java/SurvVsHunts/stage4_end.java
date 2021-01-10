package SurvVsHunts;

import org.bukkit.event.Listener;
import static org.bukkit.ChatColor.*;


public class stage4_end implements Listener {
    main plugin;

    // Stage start
    public stage4_end(main plugin) {
        this.plugin = plugin;
        if (plugin.surv_player == "") {
            plugin.say(DARK_GREEN + "Survivor wins!");
        } else {
            plugin.say(DARK_RED + "Game ends! Hunters win!");
        };
    }

}

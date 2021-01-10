package ru.FullmetalTatarin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SvhCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        sender.sendMessage("§3/svh - Show all plugin commands");
        sender.sendMessage("§3/startgame - Start game, survivor can move, hunts can move after handicap(handicap sets in config)");
        return true;
    }
}

package ru.FullmetalTatarin;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;


import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static ItemStack buildItem(Material material, int data, int amount, String name, String[] lore) {
        ItemStack stack = new ItemStack(material, amount, (short)((byte)data));
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        if (lore != null) {
            List<String> list = new ArrayList<>();
            for (String str : lore) {
                list.add(ChatColor.translateAlternateColorCodes('&', str));
            }

            meta.setLore(list);
        }

        stack.setItemMeta(meta);
        return stack;
    }

    public static ItemStack buildItem(Material material, int data, int amount, String name, List<String> lore) {
        ItemStack stack = new ItemStack(material, amount, (short)((byte)data));
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        if (lore != null) {
            List<String> list = new ArrayList<>();
            for (String str : lore) {
                list.add(ChatColor.translateAlternateColorCodes('&', str));
            }

            meta.setLore(list);
        }

        stack.setItemMeta(meta);
        return stack;
    }


    private ItemStack buildPotion(Material material, int data, int amount, String name, String[] lore, Color cl) {
        ItemStack stack = new ItemStack(material, amount, (short)0);
        PotionMeta meta = (PotionMeta)stack.getItemMeta();
        meta.setColor(cl);
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        if (lore != null) {
            List<String> list = new ArrayList<>();
            for (String str : lore) {
                list.add(ChatColor.translateAlternateColorCodes('&', str));
            }

            meta.setLore(list);
        }

        stack.setItemMeta(meta);
        return stack;
    }
}
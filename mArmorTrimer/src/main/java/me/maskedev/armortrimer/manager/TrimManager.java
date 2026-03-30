package me.maskedev.armortrimer.manager;

import me.maskedev.armortrimer.ArmorTrimer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;

public class TrimManager {

    private final ArmorTrimer plugin;

    public TrimManager(ArmorTrimer plugin) {
        this.plugin = plugin;
    }

    public boolean applyTrim(ItemStack item, String patternKey, String materialKey) {
        if (item == null || item.getType().isAir()) return false;
        if (!(item.getItemMeta() instanceof ArmorMeta armorMeta)) return false;

        TrimPattern pattern = Registry.TRIM_PATTERN.get(NamespacedKey.minecraft(patternKey.toLowerCase()));
        TrimMaterial material = Registry.TRIM_MATERIAL.get(NamespacedKey.minecraft(materialKey.toLowerCase()));

        if (pattern == null || material == null) return false;

        ArmorTrim trim = new ArmorTrim(material, pattern);
        armorMeta.setTrim(trim);
        item.setItemMeta(armorMeta);
        return true;
    }

    public boolean removeTrim(ItemStack item) {
        if (item == null || item.getType().isAir()) return false;
        if (!(item.getItemMeta() instanceof ArmorMeta armorMeta)) return false;

        armorMeta.setTrim(null);
        item.setItemMeta(armorMeta);
        return true;
    }

    public boolean isArmor(ItemStack item) {
        if (item == null) return false;
        Material type = item.getType();
        String name = type.name();
        return name.endsWith("_HELMET") || name.endsWith("_CHESTPLATE") || name.endsWith("_LEGGINGS") || name.endsWith("_BOOTS");
    }
}

package me.maskedev.armortrimer.gui;

import me.maskedev.armortrimer.ArmorTrimer;
import me.maskedev.armortrimer.config.ConfigManager;
import me.maskedev.armortrimer.util.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.Registry;
import org.bukkit.NamespacedKey;

import java.util.ArrayList;
import java.util.List;

public class TrimmerGUI implements InventoryHolder {

    private final ArmorTrimer plugin;
    private final Player player;
    private final Inventory inventory;

    private int patternIndex = 0;
    private int materialIndex = 0;

    public TrimmerGUI(ArmorTrimer plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        ConfigManager cm = plugin.getConfigManager();
        this.inventory = Bukkit.createInventory(this, 27, MessageUtils.format(cm.getGuiTitle()));
        update();
    }

    public void tickAnimation() {
    }

    public void nextPattern() {
        ConfigManager cm = plugin.getConfigManager();
        patternIndex = (patternIndex + 1) % cm.getPatterns().size();
        update();
    }

    public void nextMaterial() {
        ConfigManager cm = plugin.getConfigManager();
        materialIndex = (materialIndex + 1) % cm.getMaterials().size();
        update();
    }

    public void update() {
        inventory.clear();
        ConfigManager cm = plugin.getConfigManager();

        ItemStack filler = new ItemStack(cm.getFillerMaterial());
        ItemMeta fMeta = filler.getItemMeta();
        if (fMeta != null) {
            fMeta.displayName(net.kyori.adventure.text.Component.text(" "));
            filler.setItemMeta(fMeta);
        }
        for (int i = 0; i < inventory.getSize(); i++)
            inventory.setItem(i, filler);

        List<ConfigManager.PatternInfo> pList = new ArrayList<>(cm.getPatterns().values());
        ConfigManager.PatternInfo currentP = pList.get(patternIndex);

        Material pMat = Material.matchMaterial(currentP.icon());
        ItemStack pItem = new ItemStack(pMat != null ? pMat : Material.PAPER);
        ItemMeta pMeta = pItem.getItemMeta();
        if (pMeta != null) {
            pMeta.displayName(MessageUtils.format(currentP.displayName()));
            pItem.setItemMeta(pMeta);
        }
        inventory.setItem(11, pItem);

        List<ConfigManager.MaterialInfo> mList = new ArrayList<>(cm.getMaterials().values());
        ConfigManager.MaterialInfo currentM = mList.get(materialIndex);

        Material mMat = Material.matchMaterial(currentM.icon());
        ItemStack mItem = new ItemStack(mMat != null ? mMat : Material.IRON_INGOT);
        ItemMeta mMeta = mItem.getItemMeta();
        if (mMeta != null) {
            mMeta.displayName(MessageUtils.format(currentM.displayName()));
            mItem.setItemMeta(mMeta);
        }
        inventory.setItem(15, mItem);

        inventory.setItem(13, getPreviewItem(currentP.patternName(), currentM.materialName()));

        ItemStack apply = new ItemStack(Material.GOLD_BLOCK);
        ItemMeta aMeta = apply.getItemMeta();
        if (aMeta != null) {
            aMeta.displayName(MessageUtils.format(cm.getMessages().get("apply-button")));
            apply.setItemMeta(aMeta);
        }
        inventory.setItem(22, apply);
    }

    private ItemStack getPreviewItem(String pName, String mName) {
        ItemStack preview = player.getInventory().getItemInMainHand().clone();
        ConfigManager cm = plugin.getConfigManager();

        if (preview.getType().isAir() || !plugin.getTrimManager().isArmor(preview)) {
            preview = new ItemStack(Material.NETHERITE_CHESTPLATE);
        }

        ItemMeta meta = preview.getItemMeta();
        if (meta instanceof ArmorMeta armorMeta) {
            TrimPattern tp = Registry.TRIM_PATTERN.get(NamespacedKey.minecraft(pName.toLowerCase()));
            TrimMaterial tm = Registry.TRIM_MATERIAL.get(NamespacedKey.minecraft(mName.toLowerCase()));

            if (tp != null && tm != null) {
                armorMeta.setTrim(new ArmorTrim(tm, tp));
            }
            armorMeta.displayName(MessageUtils.format(cm.getMessages().get("preview-name")));
            preview.setItemMeta(armorMeta);
        }
        return preview;
    }

    public String getSelectedPatternKey() {
        return new ArrayList<>(plugin.getConfigManager().getPatterns().keySet()).get(patternIndex);
    }

    public String getSelectedMaterialKey() {
        return new ArrayList<>(plugin.getConfigManager().getMaterials().keySet()).get(materialIndex);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
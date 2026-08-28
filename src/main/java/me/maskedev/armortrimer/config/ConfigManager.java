package me.maskedev.armortrimer.config;

import me.maskedev.armortrimer.ArmorTrimer;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class ConfigManager {

    private final ArmorTrimer plugin;
    private FileConfiguration config;

    private String guiTitle;
    private int guiSize;
    private Material fillerMaterial;

    private final Map<String, PatternInfo> patterns = new LinkedHashMap<>();
    private final Map<String, MaterialInfo> materials = new LinkedHashMap<>();
    private final Map<String, GroupInfo> groups = new LinkedHashMap<>();
    private final Map<String, String> messages = new HashMap<>();

    public ConfigManager(ArmorTrimer plugin) {
        this.plugin = plugin;
        load();
    }

    public void load() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        this.config = plugin.getConfig();

        this.guiTitle = config.getString("gui.title", "<gold>Armor Trimer");
        this.guiSize = config.getInt("gui.size", 27);

        String configMaterial = config.getString("gui.filler_material", "YELLOW_STAINED_GLASS_PANE");

        try {
            this.fillerMaterial = Material.valueOf(configMaterial.toUpperCase());
        } catch (Exception e) {
            // Minecraft'ta GOLD_STAINED_GLASS_PANE yoktur, sarı (YELLOW) kullanılır.
            this.fillerMaterial = Material.YELLOW_STAINED_GLASS_PANE;
        }

        patterns.clear();
        ConfigurationSection patternsSection = config.getConfigurationSection("patterns");
        if (patternsSection != null) {
            for (String key : patternsSection.getKeys(false)) {
                patterns.put(key, new PatternInfo(
                        patternsSection.getString(key + ".display-name"),
                        patternsSection.getString(key + ".icon"),
                        key.toUpperCase()));
            }
        }

        materials.clear();
        ConfigurationSection materialsSection = config.getConfigurationSection("materials");
        if (materialsSection != null) {
            for (String key : materialsSection.getKeys(false)) {
                materials.put(key, new MaterialInfo(
                        materialsSection.getString(key + ".display-name"),
                        materialsSection.getString(key + ".icon"),
                        key.toUpperCase()));
            }
        }

        messages.clear();
        ConfigurationSection messagesSection = config.getConfigurationSection("messages");
        if (messagesSection != null) {
            for (String key : messagesSection.getKeys(false)) {
                messages.put(key, messagesSection.getString(key));
            }
        }

        groups.clear();
        ConfigurationSection groupsSection = config.getConfigurationSection("groups");
        if (groupsSection != null) {
            for (String key : groupsSection.getKeys(false)) {
                groups.put(key, new GroupInfo(
                        groupsSection.getInt(key + ".priority"),
                        groupsSection.getStringList(key + ".allowed_patterns"),
                        groupsSection.getStringList(key + ".allowed_materials")));
            }
        }
    }

    public String getGuiTitle() {
        return guiTitle;
    }

    public int getGuiSize() {
        return guiSize;
    }

    public Material getFillerMaterial() {
        return fillerMaterial;
    }

    public Map<String, PatternInfo> getPatterns() {
        return patterns;
    }

    public Map<String, MaterialInfo> getMaterials() {
        return materials;
    }

    public Map<String, String> getMessages() {
        return messages;
    }

    public Map<String, GroupInfo> getGroups() {
        return groups;
    }

    public static record PatternInfo(String displayName, String icon, String patternName) {
    }

    public static record MaterialInfo(String displayName, String icon, String materialName) {
    }

    public static record GroupInfo(int priority, List<String> allowedPatterns, List<String> allowedMaterials) {
    }
}
package me.maskedev.armortrimer;

import com.tcoded.folialib.FoliaLib;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.maskedev.armortrimer.config.ConfigManager;
import me.maskedev.armortrimer.gui.TrimmerGUI;
import me.maskedev.armortrimer.listener.GUIListener;
import me.maskedev.armortrimer.manager.TrimManager;
import me.maskedev.armortrimer.util.MessageUtils;
import org.bstats.bukkit.Metrics;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

public class ArmorTrimer extends JavaPlugin {

    private ConfigManager configManager;
    private TrimManager trimManager;
    private FoliaLib foliaLib;
    private final Set<TrimmerGUI> activeGuis = Collections.newSetFromMap(new WeakHashMap<>());

    @Override
    public void onEnable() {
        this.foliaLib = new FoliaLib(this);
        this.configManager = new ConfigManager(this);
        this.trimManager = new TrimManager(this);

        getServer().getPluginManager().registerEvents(new GUIListener(this), this);

        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register("trim", "mArmorTrimer ana komutu", List.of("trimer", "armortrim"),
                    new TrimCommand());
        });

        foliaLib.getScheduler().runTimer(() -> {
            activeGuis.forEach(gui -> gui.tickAnimation());
        }, 1L, 20L);

        new Metrics(this, 30486);
        getLogger().info("mArmorTrimer has been enabled!");
    }

    public void addActiveGui(TrimmerGUI gui) {
        activeGuis.add(gui);
    }

    private class TrimCommand implements BasicCommand {
        @Override
        public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
            CommandSender sender = stack.getSender();

            if (!(sender instanceof Player player)) {
                sender.sendMessage("Only players can use this command.");
                return;
            }

            if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                if (!player.hasPermission("armortrimer.admin")) {
                    player.sendMessage(MessageUtils.format(configManager.getMessages().get("no-perm")));
                    return;
                }

                foliaLib.getScheduler().runAsync((task) -> {
                    configManager.load();
                    player.sendMessage(MessageUtils.format("<green>Configuration reloaded asynchronously!"));
                });
                return;
            }

            if (!player.hasPermission("armortrimer.use")) {
                player.sendMessage(MessageUtils.format(configManager.getMessages().get("no-perm")));
                return;
            }

            TrimmerGUI gui = new TrimmerGUI(ArmorTrimer.this, player);
            player.openInventory(gui.getInventory());
        }

        @Override
        public @NotNull List<String> suggest(@NotNull CommandSourceStack stack, @NotNull String[] args) {
            if (args.length == 1 && stack.getSender().hasPermission("armortrimer.admin")) {
                return List.of("reload");
            }
            return List.of();
        }
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public TrimManager getTrimManager() {
        return trimManager;
    }

    public FoliaLib getFoliaLib() {
        return foliaLib;
    }
}
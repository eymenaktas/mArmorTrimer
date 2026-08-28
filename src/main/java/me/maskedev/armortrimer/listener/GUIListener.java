package me.maskedev.armortrimer.listener;

import me.maskedev.armortrimer.ArmorTrimer;
import me.maskedev.armortrimer.gui.TrimmerGUI;
import me.maskedev.armortrimer.util.MessageUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GUIListener implements Listener {

    private final ArmorTrimer plugin;
    // Folia için ConcurrentHashMap daha güvenlidir
    private final Map<UUID, Long> lastClick = new ConcurrentHashMap<>();
    // 80ms delay (nanosaniye cinsinden 80 * 1,000,000)
    private final long DELAY_NS = 80L * 1_000_000L;

    public GUIListener(ArmorTrimer plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof TrimmerGUI gui))
            return;

        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        long now = System.nanoTime();

        // Spam Kontrolü - Eğer süre dolmadıysa direkt işlemi bitir
        if (lastClick.containsKey(uuid) && (now - lastClick.get(uuid) < DELAY_NS)) {
            return;
        }
        lastClick.put(uuid, now);

        int slot = event.getRawSlot();

        if (slot == 11) {
            gui.nextPattern();
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3f, 1.5f);
        } else if (slot == 15) {
            gui.nextMaterial();
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3f, 1.5f);
        } else if (slot == 22) {
            handleApply(player, gui);
        }
    }

    private void handleApply(Player player, TrimmerGUI gui) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!plugin.getTrimManager().isArmor(item)) {
            player.sendMessage(MessageUtils.format(plugin.getConfigManager().getMessages().get("no-armor")));
            return;
        }

        String pKey = gui.getSelectedPatternKey();
        String mKey = gui.getSelectedMaterialKey();

        if (plugin.getTrimManager().applyTrim(item, pKey, mKey)) {
            player.sendMessage(MessageUtils.format(plugin.getConfigManager().getMessages().get("applied")));
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1f, 1f);
            player.closeInventory();
        }
    }
}
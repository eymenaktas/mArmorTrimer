package me.maskedev.armortrimer.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class MessageUtils {
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    public static Component format(String message) {
        if (message == null)
            return Component.empty();
        String clean = message.replace("&", "§").replaceAll("§[0-9a-fk-or]", "");
        return MINI_MESSAGE.deserialize("<!italic>" + clean);
    }
}
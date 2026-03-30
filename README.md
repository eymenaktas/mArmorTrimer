![thumbnail](https://cdn.modrinth.com/data/cached_images/59d9a78d59eda81d0c8ccafaab62998b9cc22852_0.webp)

![Author](https://img.shields.io/badge/Author-MaskeDev-gold?style=for-the-badge)
![Version](https://img.shields.io/badge/Version-1.0.0-gold?style=for-the-badge)
![Folia](https://img.shields.io/badge/Folia-Supported-gold?style=for-the-badge)
[![bStats](https://img.shields.io/badge/bStats-Statistics-gold?style=for-the-badge)](https://bstats.org/plugin/bukkit/mArmorTrimer/30486)

**Armor Trimer** is a high-performance, premium armor customization plugin designed for modern Minecraft servers (1.20 - 1.21.1). It allows players to customize their armor with unique patterns and materials through a beautiful, user-friendly GUI.

---

## ✨ Features

- 🛠️ **Universal Compatibility**: Supports Minecraft 1.20 up to 1.21.1.
- 🚀 **Folia Support**: Built for high-performance servers with multi-threaded region systems.
- 💎 **Premium GUI**: A stunning 27-slot menu (or configurable) with a yellow/gold theme.
- 🌈 **Rich Formatting**: Full support for **MiniMessage** (`<gradient>`, `<bold>`, etc.) and legacy color codes (`&f`, `&hex`).
- 👥 **Group System**: Define which ranks can use specific patterns and materials.
- ⚡ **Zero-Tick Lag**: Optimized for minimal performance impact.

---

## 📜 Commands & Aliases

- `/trim` - Opens the armor trim menu.
- `/trimer` (Alias)
- `/armortrim` (Alias)
- `/trim reload` - Reloads the plugin configuration.

---

## 🔑 Permissions

- `armortrimer.use` - Allows players to open the GUI. (Default: true)
- `armortrimer.admin` - Allows reloading the config. (Default: OP)
- `armortrimer.group.default` - Access to default group patterns/materials.
- `armortrimer.group.vip` - Access to VIP group patterns/materials.
- `armortrimer.group.admin` - Access to Admin group patterns/materials.

---

## ⚙️ Configuration

The configuration is simple and powerful. You can customize the entire GUI, messages, and permission groups.

```yaml
# vtCore - Armor Trimer Configuration
gui:
  title: "<gradient:#ff8cff:#8cffff><bold>Armor Trimer"
  size: 27
  filler_material: "YELLOW_STAINED_GLASS_PANE"
```

---

## 👨‍💻 Author

Created with ❤️ by **MaskeDev**.

---

## 📊 Statistics

![bStats Statistics](https://bstats.org/signatures/bukkit/mArmorTrimer.svg)

This plugin uses bStats to collect anonymous data to help improve the plugin. You can opt-out in the bStats configuration.

<div align="center">

# QuantumMC+ V1.0.0

Modern Minecraft utility client for Fabric 1.21.x by [@DriizzyyB](https://github.com/driizzyy)

Originally ported from [Oyvey](https://github.com/mioclient/oyvey-ported).

<img src="images/ui.png" width="90%" />

</div>

---

## Features

### Combat
- **Aura** — Attacks nearest entity in range with configurable target filtering
- **AutoTotem** — Automatically equips a totem when health drops below threshold
- **Criticals** — Ensures every hit registers as a critical
- **KeyPearl** — Throws an ender pearl from inventory with a single keybind

### Movement
- **Speed** — Strafe-based speed multiplier
- **Step** — Auto-step up blocks
- **Sprint** — Automatic sprinting (multi-directional support)
- **SafeWalk** — Prevents falling off edges
- **ReverseStep** — Pulls you down when on ground

### Render
- **Fullbright** — Increases world brightness
- **ESP** — Highlights entities with colored bounding boxes
- **Tracers** — Draws lines/boxes toward nearby entities
- **BlockHighlight** — Wireframe overlay on looked-at block

### Player
- **AutoTool** — Automatically switches to best tool when mining
- **AutoEat** — Automatically eats when hunger is low
- **AutoRepair** — Detects and warns on low-durability items
- **FastPlace** — Removes right-click delay
- **NoFall** — Negates fall damage via packet spoofing
- **Velocity** — Reduces/cancels knockback

### HUD
- **Coordinates** — XYZ position with nether coordinate conversion
- **FPS** — Frames-per-second counter
- **Status** — Health and hunger display
- **Watermark** — Client name and version overlay

### Client
- **Notifications** — Configurable module toggle notifications
- **NotificationOverlay** — On-screen toast notifications

### Misc
- **MCF** — Middle-click to add/remove friends
- **ChatFilter** — Chat message filtering (placeholder)

---

## Default Keybinds

| Key | Action |
|---|---|
| `R-SHIFT` | Open ClickGUI |
| `R-CONTROL` | Open HUD Editor |
| `.` (period) | Command prefix |

Configure keybinds per-module via ClickGUI or the `.bind` command.

---

## Commands

| Command | Description |
|---|---|
| `.bind <module> <key>` | Bind a key to a module |
| `.toggle <module>` | Toggle a module on/off |
| `.friend <add/remove/list/clear>` | Manage friends list |
| `.drawn <module>` | Show/hide a module in the array list |
| `.prefix <char>` | Change the command prefix |
| `.profile [list/set/delete]` | Manage config profiles |
| `.help [page]` | List all commands |
| `.<module>` | Configure any module's settings via chat |

---

## Installation

1. Install **Minecraft 1.21.11**
2. Install **Fabric Loader 0.18.4**
3. Download the **QuantumMC+** JAR from [Releases](https://github.com/driizzyy/QuantumMC-plus/releases)
4. Place the JAR into your `.minecraft/mods/` folder
5. (Optional) Install **Fabric API 0.141.1+1.21.11** for compatibility

---

## Building from Source

```bash
git clone https://github.com/driizzyy/QuantumMC-plus.git
cd QuantumMC-plus
./gradlew build
```

The built JAR will be in `build/libs/`.

---

## Contributing

Contributions are welcome! Please see [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines.

---

## License

This project is licensed under the MIT License — see [LICENSE](LICENSE) for details.

---

## Links

- [QuantumMC+ Repository](https://github.com/driizzyy/QuantumMC-plus)
- [Oyvey Ported (Original)](https://github.com/mioclient/oyvey-ported)

# MaazX Ultra

MaazX Ultra is a small client-side visibility and status mod for Fabric on Minecraft Java Edition 1.20.1, with a Windows companion for installing the mod and opening the official Minecraft Launcher.

## Features

- Client-side entity render-distance culling. It skips drawing non-player entities outside the configured range; it does not change entity AI, ticking, simulation, or server state.
- Compact FPS and JVM-memory HUD, toggleable with **F3+M**.
- Mod Menu settings for the HUD and entity culling, plus a selectable 16–128 block cull range.
- Lightweight Windows companion that installs the bundled mod jar in the standard `.minecraft/mods` folder and opens the official launcher.

## Requirements

- Minecraft Java Edition 1.20.1 and Fabric Loader.
- Java 21 or newer to run this build of the mod.
- Windows 10/11 for the graphical companion. The companion does not replace Minecraft authentication or the official launcher.
- Mod Menu is optional and enables the in-game settings screen.

Sodium and other compatible performance mods may be used separately. Check each modpack's compatibility notes before changing its mod list.

## Install

1. Install Fabric Loader for Minecraft 1.20.1 and start that profile once.
2. Run `MaazXLauncher-Setup.exe`, then open MaazXLauncher and choose **Install MaazXClient**; or copy `maazxclient-1.0.0.jar` to `.minecraft/mods`.
3. Start the Fabric 1.20.1 profile using the official Minecraft Launcher.
4. If installed, open **Mods → MaazXClient → Configure** to change settings.

## Configuration

The mod creates `config/maazxclient.properties` on first run:

| Setting | Default | Meaning |
| --- | --- | --- |
| `hud_enabled` | `true` | Show FPS and used JVM heap in the top-right corner. |
| `entity_culling_enabled` | `true` | Skip rendering non-player entities beyond the configured distance. |
| `entity_cull_distance` | `48` | Render distance in blocks for non-player entities; accepted range is 16–128. |

The HUD toggle is **F3+M**. Entity culling affects visuals only and does not reduce entity ticks or server work.

## Performance notes

No fixed FPS increase is promised. Actual performance depends on the modpack, world, display resolution, graphics driver, Java runtime, and background applications. The 85–100 FPS goal has not been benchmarked and should not be treated as a measured result.

Minecraft Java Edition renders through OpenGL via LWJGL. This project does not provide a D3D11 renderer, Vulkan support, a GL-to-D3D interception layer, or a replacement chunk/lighting engine. The described Ironlake GPU's OpenGL 2.1 driver therefore remains part of the rendering path. Lowering Minecraft's render distance, resolution, and graphics settings and closing browser/recording applications can help diagnose bottlenecks, but results vary.

For a 4 GB system, do not reserve all memory for Minecraft. Start with the modpack's documented memory settings; increasing heap size can worsen paging on a system with limited RAM and a mechanical drive.

## Build

Build the Fabric mod with Java 21:

```sh
./gradlew build
```

The mod jar is written to `mod/build/libs/maazxclient-1.0.0.jar`.

Build the companion on Windows with CMake and Visual Studio C++:

```powershell
cmake -S launcher -B launcher/build -A x64
cmake --build launcher/build --config Release
```

Build the installer with Inno Setup 6 after building both artifacts:

```powershell
iscc installer\setup.iss
```

Tagged `v*` pushes build and attach the mod, launcher, and installer to a GitHub release.

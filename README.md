# Pane Seam Fix

A client-side Fabric mod for Minecraft 26.1–26.3. It hides the doubled faces where glass of different types meets, so mixed-color glass builds render cleanly. This matters most with shaders that refract translucent surfaces. It covers:

- **Glass panes:** the clear pane and the 16 stained panes.
- **Full glass blocks:** clear, tinted, and the 16 stained colors. *(Added in 1.1.0.)*

## Why the seams appear

Vanilla hides the faces where two pieces of glass touch only when both are the exact same block:

| Glass | Culling check | Vanilla rule |
|---|---|---|
| Panes | `IronBarsBlock.skipRendering` | Same block, or both in the `minecraft:bars` tag (iron and copper bars) |
| Full blocks | `HalfTransparentBlock.skipRendering` | Same block only (`neighborState.is(this)`) |

Two different kinds of glass, such as red and blue, or clear and stained, therefore each draw a face at the shared boundary. Those doubled faces show through the glass. Sodium uses the same `BlockState.skipRendering` check, so it behaves the same way.

## What the mod changes

One `@Inject` at the head of each culling method applies the vanilla same-block rule to pairs of *different* vanilla glass:

- **Panes** (`IronBarsBlockMixin`):
  - **Horizontal:** the face is hidden when both panes extend an arm into the shared boundary.
  - **Vertical:** the face is hidden, the same as vanilla does for identical panes. Vanilla pane models have no cullface on their top and bottom faces, so this only takes effect when a resource pack adds one, such as Continuity's "Glass Pane Culling Fix".
- **Full blocks** (`HalfTransparentBlockMixin`): the shared face is hidden in all six directions. Colors still blend when you look through two blocks, because each block's outer faces are still drawn.

The rules live in `PaneSeamLogic`. The mod leaves these on vanilla behavior:

- same-block pairs
- iron and copper bars
- ice, slime, honey, and copper grates, which share `HalfTransparentBlock`
- a pane touching a full glass block
- modded glass

## Layout

| Path | Purpose |
|---|---|
| `src/main/java/paneseamfix/PaneSeamLogic.java` | The culling rules |
| `src/main/java/paneseamfix/mixin/IronBarsBlockMixin.java` | Hooks the pane rule into `IronBarsBlock.skipRendering` |
| `src/main/java/paneseamfix/mixin/HalfTransparentBlockMixin.java` | Hooks the block rule into `HalfTransparentBlock.skipRendering` |
| `src/main/resources/` | `fabric.mod.json` and the mixin config |
| `stubs/` | Compile-time stand-ins for the Minecraft and Mixin classes used; never packaged |
| `test/` | Runs the real mixin handlers ahead of a copy of 26.3's culling logic |
| `build.sh` | Compiles, tests, and packages `dist/pane-seam-fix-<version>.jar` |

## Building

You need JDK 17 or newer. No Gradle and no downloads are required.

```sh
./build.sh
```

Minecraft 26.x ships unobfuscated, and Fabric runs it under Mojang's own names. The mixins therefore target names directly, with `remap = false` and no refmap. The stubs only have to match the real signatures: the same class names, method names, and descriptors, plus the real retention for each Mixin annotation. They were checked against the decompiled 26.1, 26.2 and 26.3 client source.

## Porting to a newer Minecraft version

1. Check that `IronBarsBlock.skipRendering(BlockState, BlockState, Direction)` and `HalfTransparentBlock.skipRendering(BlockState, BlockState, Direction)` still exist and still use the same-block logic.
2. Check that `TransparentBlock`, `StainedGlassBlock`, and `TintedGlassBlock` still inherit `HalfTransparentBlock.skipRendering` without overriding it.
3. Check the other references in `PaneSeamLogic`:
   - `CrossCollisionBlock.PROPERTY_BY_DIRECTION`
   - `StainedGlassPaneBlock` and `StainedGlassBlock`
   - `Blocks.GLASS_PANE`, `Blocks.GLASS`, and `Blocks.TINTED_GLASS`
   - `Direction.getAxis()`, `Direction.getOpposite()`, and `Direction.Axis.isHorizontal()`
4. Add the new version to `depends.minecraft` in `fabric.mod.json`.

If you have network access to Fabric's Maven, you can also move the project to a standard Fabric Loom build (the `net.fabricmc.fabric-loom` plugin for 26.x) and delete `stubs/`.

## Changelog

- **1.1.0:** Full glass blocks: clear, tinted, and stained glass no longer draw doubled faces against a different kind of glass.
- **1.0.0:** Glass panes: different-colored panes no longer draw doubled end faces at the joint.

## License

MIT. See `LICENSE`.

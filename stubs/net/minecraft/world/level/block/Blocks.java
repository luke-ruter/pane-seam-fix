package net.minecraft.world.level.block;

// Compile-time stand-in mirroring Minecraft 26.3 (GLASS, TINTED_GLASS and GLASS_PANE are declared as Block).
public class Blocks {
    public static final Block GLASS = new TransparentBlock("glass");
    public static final Block TINTED_GLASS = new TintedGlassBlock("tinted_glass");
    public static final Block GLASS_PANE = new IronBarsBlock("glass_pane", false);

    // Test-only extras (the real class has these under other shapes).
    public static final Block RED_STAINED_GLASS = new StainedGlassBlock("red_stained_glass");
    public static final Block BLUE_STAINED_GLASS = new StainedGlassBlock("blue_stained_glass");
    public static final Block BLUE_ICE = new HalfTransparentBlock("blue_ice");
    public static final Block RED_STAINED_GLASS_PANE = new StainedGlassPaneBlock("red_stained_glass_pane");
    public static final Block BLUE_STAINED_GLASS_PANE = new StainedGlassPaneBlock("blue_stained_glass_pane");
    public static final Block IRON_BARS = new IronBarsBlock("iron_bars", true);
    public static final Block COPPER_BARS = new IronBarsBlock("copper_bars", true);
}

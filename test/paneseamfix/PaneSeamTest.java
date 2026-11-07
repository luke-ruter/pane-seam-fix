package paneseamfix;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paneseamfix.mixin.HalfTransparentBlockMixin;
import paneseamfix.mixin.IronBarsBlockMixin;

/**
 * Runs the real mixin handlers (via reflection) ahead of the vanilla 26.3 culling logic, the way the
 * HEAD injections do at runtime, and checks the combined result.
 */
public final class PaneSeamTest {
    private static int failures = 0;
    private static Method paneHandler;
    private static Method blockHandler;
    private static IronBarsBlockMixin paneMixin;
    private static HalfTransparentBlockMixin blockMixin;

    public static void main(String[] args) throws Exception {
        paneMixin = new IronBarsBlockMixin() { };
        paneHandler = IronBarsBlockMixin.class.getDeclaredMethod("paneseamfix$cullBetweenGlassPanes",
                BlockState.class, BlockState.class, Direction.class, CallbackInfoReturnable.class);
        paneHandler.setAccessible(true);

        blockMixin = new HalfTransparentBlockMixin() { };
        blockHandler = HalfTransparentBlockMixin.class.getDeclaredMethod("paneseamfix$cullBetweenGlassBlocks",
                BlockState.class, BlockState.class, Direction.class, CallbackInfoReturnable.class);
        blockHandler.setAccessible(true);

        Block red = Blocks.RED_STAINED_GLASS_PANE;
        Block blue = Blocks.BLUE_STAINED_GLASS_PANE;
        Block clear = Blocks.GLASS_PANE;
        Block iron = Blocks.IRON_BARS;
        Block copper = Blocks.COPPER_BARS;

        System.out.println("-- Panes");
        // A straight east-west run: west pane connects east, east pane connects west.
        check("red|blue joined, red's east face", red, "ew", blue, "ew", Direction.EAST, true, false);
        check("red|blue joined, blue's west face", blue, "ew", red, "ew", Direction.WEST, true, false);
        check("red|red joined (vanilla path unchanged)", red, "ew", red, "ew", Direction.EAST, true, true);
        check("clear|red joined", clear, "ew", red, "ew", Direction.EAST, true, false);
        check("red|clear joined", red, "ew", clear, "ew", Direction.EAST, true, false);
        check("red|blue but red has no east arm", red, "ns", blue, "ew", Direction.EAST, false, false);
        check("red|blue but blue has no west arm", red, "ew", blue, "ns", Direction.EAST, false, false);
        check("red over blue (vertical)", red, "ew", blue, "ew", Direction.DOWN, true, false);
        check("red under blue (vertical)", red, "ew", blue, "ew", Direction.UP, true, false);
        check("pane next to iron bars (untouched)", red, "ew", iron, "ew", Direction.EAST, false, false);
        check("iron bars next to pane (untouched)", iron, "ew", red, "ew", Direction.EAST, false, false);
        check("iron|copper bars (vanilla bars rule)", iron, "ew", copper, "ew", Direction.EAST, true, true);

        Block redBlock = Blocks.RED_STAINED_GLASS;
        Block blueBlock = Blocks.BLUE_STAINED_GLASS;
        Block glass = Blocks.GLASS;
        Block tinted = Blocks.TINTED_GLASS;
        Block ice = Blocks.BLUE_ICE;

        System.out.println("-- Full blocks");
        for (Direction d : Direction.values()) {
            check("red|blue block, " + d, redBlock, "", blueBlock, "", d, true, false);
        }
        check("red|red block (vanilla path unchanged)", redBlock, "", redBlock, "", Direction.EAST, true, true);
        check("clear|clear block (vanilla path unchanged)", glass, "", glass, "", Direction.UP, true, true);
        check("clear|red block", glass, "", redBlock, "", Direction.EAST, true, false);
        check("red|clear block", redBlock, "", glass, "", Direction.WEST, true, false);
        check("tinted|clear block", tinted, "", glass, "", Direction.NORTH, true, false);
        check("red|tinted block", redBlock, "", tinted, "", Direction.SOUTH, true, false);
        check("red block next to blue ice (untouched)", redBlock, "", ice, "", Direction.EAST, false, false);
        check("blue ice next to red block (untouched)", ice, "", redBlock, "", Direction.WEST, false, false);
        check("blue ice|blue ice (vanilla path unchanged)", ice, "", ice, "", Direction.UP, true, true);
        check("red block next to red pane (untouched)", redBlock, "", red, "ew", Direction.EAST, false, false);

        if (failures == 0) {
            System.out.println("ALL TESTS PASSED");
        } else {
            System.out.println(failures + " TEST(S) FAILED");
            System.exit(1);
        }
    }

    private static BlockState state(Block block, String arms) {
        Map<Property<?>, Comparable<?>> values = new HashMap<>();
        if (block instanceof CrossCollisionBlock) {
            values.put(CrossCollisionBlock.NORTH, arms.contains("n"));
            values.put(CrossCollisionBlock.SOUTH, arms.contains("s"));
            values.put(CrossCollisionBlock.EAST, arms.contains("e"));
            values.put(CrossCollisionBlock.WEST, arms.contains("w"));
        }
        return block.state(values);
    }

    private static void check(String name, Block self, String selfArms, Block neighbor, String neighborArms,
                              Direction dir, boolean expectedWithMod, boolean expectedVanilla) throws Exception {
        BlockState a = state(self, selfArms);
        BlockState b = state(neighbor, neighborArms);

        boolean vanilla = a.skipRendering(b, dir);

        // At runtime each HEAD injection only runs inside its own target method, so pick the handler
        // for whichever class's skipRendering this block inherits.
        CallbackInfoReturnable<Boolean> cir = new CallbackInfoReturnable<>("skipRendering", true);
        if (self instanceof net.minecraft.world.level.block.IronBarsBlock) {
            paneHandler.invoke(paneMixin, a, b, dir, cir);
        } else if (self instanceof net.minecraft.world.level.block.HalfTransparentBlock) {
            blockHandler.invoke(blockMixin, a, b, dir, cir);
        }
        boolean withMod = cir.isCancelled() ? cir.getReturnValue() : vanilla;

        boolean ok = withMod == expectedWithMod && vanilla == expectedVanilla;
        System.out.printf("%-4s %-45s vanilla=%-5s withMod=%-5s%n", ok ? "ok" : "FAIL", name, vanilla, withMod);
        if (!ok) {
            failures++;
        }
    }
}

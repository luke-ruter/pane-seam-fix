package paneseamfix;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.level.block.StainedGlassBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

/**
 * Face-culling rules for glass of different types.
 *
 * <p>Vanilla only hides the faces where two glass blocks or panes touch when both are the exact
 * same block. Two different kinds of glass, such as red and blue stained glass, or clear and stained
 * glass, therefore each draw a face at the shared boundary. Those doubled faces show through the
 * glass, and shaders that refract translucent surfaces turn them into a visible distortion at every
 * color seam.
 *
 * <ul>
 *   <li>Panes: {@code IronBarsBlock.skipRendering} culls only the same block, or two bars.</li>
 *   <li>Full blocks: {@code HalfTransparentBlock.skipRendering} culls only the same block.</li>
 * </ul>
 *
 * <p>These rules extend vanilla's same-block behavior to any pair of vanilla glass panes, and to any
 * pair of vanilla glass blocks. Same-block pairs are left untouched, so vanilla handles them exactly
 * as before.
 */
public final class PaneSeamLogic {
    private PaneSeamLogic() {
    }

    /**
     * Returns {@code true} when the face of the pane {@code state} pointing toward {@code direction}
     * should be hidden because {@code neighborState} is a different glass pane joined to it.
     */
    public static boolean shouldCullBetweenPanes(BlockState state, BlockState neighborState, Direction direction) {
        Block self = state.getBlock();
        Block other = neighborState.getBlock();

        // Identical blocks are already culled by vanilla; leave that path alone.
        if (self == other || !isGlassPane(self) || !isGlassPane(other)) {
            return false;
        }

        // Same as vanilla for identical panes: stacked panes always cull their shared top/bottom.
        // (Vanilla pane models carry no cullface on up/down, so this only matters when a resource
        // pack such as Continuity's "Glass Pane Culling Fix" adds one.)
        if (!direction.getAxis().isHorizontal()) {
            return true;
        }

        // Horizontal: cull only when both panes actually extend an arm into the shared boundary.
        BooleanProperty outward = CrossCollisionBlock.PROPERTY_BY_DIRECTION.get(direction);
        BooleanProperty inward = CrossCollisionBlock.PROPERTY_BY_DIRECTION.get(direction.getOpposite());
        return state.getValue(outward) && neighborState.getValue(inward);
    }

    /**
     * Returns {@code true} when the face of the glass block {@code state} pointing toward
     * {@code direction} should be hidden because {@code neighborState} is a different glass block.
     * Full blocks share an entire face with every neighbor, so this matches vanilla's same-block rule
     * in all six directions.
     */
    public static boolean shouldCullBetweenGlassBlocks(BlockState state, BlockState neighborState, Direction direction) {
        Block self = state.getBlock();
        Block other = neighborState.getBlock();

        // Identical blocks are already culled by vanilla; leave that path alone.
        return self != other && isGlassBlock(self) && isGlassBlock(other);
    }

    /** Vanilla glass panes only: the clear pane and the sixteen stained panes. Bars are excluded. */
    static boolean isGlassPane(Block block) {
        return block instanceof StainedGlassPaneBlock || block == Blocks.GLASS_PANE;
    }

    /**
     * Vanilla full glass blocks only: clear, tinted and the sixteen stained colors. Other blocks that
     * share {@code HalfTransparentBlock}, such as ice, slime, honey and copper grates, are excluded.
     */
    static boolean isGlassBlock(Block block) {
        return block instanceof StainedGlassBlock || block == Blocks.GLASS || block == Blocks.TINTED_GLASS;
    }
}

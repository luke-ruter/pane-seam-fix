package net.minecraft.world.level.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

// Compile-time stand-in. skipRendering reproduces the Minecraft 26.3 logic, with the
// BlockTags.BARS check modelled by the `bars` flag (vanilla tag = iron bars + copper bars).
public class IronBarsBlock extends CrossCollisionBlock {
    private final boolean bars;

    public IronBarsBlock(String id, boolean bars) {
        super(id);
        this.bars = bars;
    }

    @Override
    protected boolean skipRendering(final BlockState state, final BlockState neighborState, final Direction direction) {
        boolean neighborIsBars = neighborState.getBlock() instanceof IronBarsBlock b && b.bars;
        boolean selfIsBars = state.getBlock() instanceof IronBarsBlock s && s.bars;
        if (neighborState.getBlock() == this
                || neighborIsBars && selfIsBars && neighborState.hasProperty(PROPERTY_BY_DIRECTION.get(direction.getOpposite()))) {
            if (!direction.getAxis().isHorizontal()) {
                return true;
            }
            if (state.getValue(PROPERTY_BY_DIRECTION.get(direction))
                    && neighborState.getValue(PROPERTY_BY_DIRECTION.get(direction.getOpposite()))) {
                return true;
            }
        }
        return super.skipRendering(state, neighborState, direction);
    }
}

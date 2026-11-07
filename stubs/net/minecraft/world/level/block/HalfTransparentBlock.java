package net.minecraft.world.level.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

// Compile-time stand-in. skipRendering reproduces the Minecraft 26.3 logic
// (neighborState.is(this) is modelled as a block identity check).
public class HalfTransparentBlock extends Block {
    public HalfTransparentBlock(String id) {
        super(id);
    }

    @Override
    protected boolean skipRendering(final BlockState state, final BlockState neighborState, final Direction direction) {
        return neighborState.getBlock() == this ? true : super.skipRendering(state, neighborState, direction);
    }
}

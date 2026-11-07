package net.minecraft.world.level.block.state;

import java.util.Map;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;

// Compile-time stand-in mirroring Minecraft 26.3.
public abstract class BlockBehaviour {
    protected boolean skipRendering(final BlockState state, final BlockState neighborState, final Direction direction) {
        return false;
    }

    public abstract static class BlockStateBase extends StateHolder<Block, BlockState> {
        protected BlockStateBase(Block owner, Map<Property<?>, Comparable<?>> values) {
            super(owner, values);
        }

        public Block getBlock() {
            return this.owner;
        }

        protected abstract BlockState asState();

        public boolean skipRendering(final BlockState neighborState, final Direction direction) {
            return this.getBlock().skipRendering(this.asState(), neighborState, direction);
        }
    }
}

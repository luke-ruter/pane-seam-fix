package net.minecraft.world.level.block.state;

import java.util.Map;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;

// Compile-time stand-in mirroring Minecraft 26.3.
public class BlockState extends BlockBehaviour.BlockStateBase {
    public BlockState(Block owner, Map<Property<?>, Comparable<?>> values) {
        super(owner, values);
    }

    @Override
    protected BlockState asState() {
        return this;
    }
}

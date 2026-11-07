package net.minecraft.world.level.block;

import java.util.Map;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

// Compile-time stand-in mirroring Minecraft 26.3.
public class Block extends BlockBehaviour {
    private final String id;

    public Block(String id) {
        this.id = id;
    }

    /** Test helper (not part of the real API). */
    public BlockState state(Map<Property<?>, Comparable<?>> values) {
        return new BlockState(this, values);
    }

    @Override
    public String toString() {
        return "Block{" + this.id + "}";
    }
}

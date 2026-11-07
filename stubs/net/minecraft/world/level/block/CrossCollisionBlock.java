package net.minecraft.world.level.block;

import java.util.EnumMap;
import java.util.Map;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

// Compile-time stand-in mirroring Minecraft 26.3.
public abstract class CrossCollisionBlock extends Block {
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty WEST = BooleanProperty.create("west");
    public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION;

    static {
        Map<Direction, BooleanProperty> map = new EnumMap<>(Direction.class);
        map.put(Direction.NORTH, NORTH);
        map.put(Direction.EAST, EAST);
        map.put(Direction.SOUTH, SOUTH);
        map.put(Direction.WEST, WEST);
        PROPERTY_BY_DIRECTION = map;
    }

    protected CrossCollisionBlock(String id) {
        super(id);
    }
}

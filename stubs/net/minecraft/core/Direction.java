package net.minecraft.core;

// Compile-time stand-in. Signatures mirror Minecraft 26.3 (net.minecraft.core.Direction).
public enum Direction {
    DOWN, UP, NORTH, SOUTH, WEST, EAST;

    public Direction getOpposite() {
        switch (this) {
            case DOWN: return UP;
            case UP: return DOWN;
            case NORTH: return SOUTH;
            case SOUTH: return NORTH;
            case WEST: return EAST;
            default: return WEST;
        }
    }

    public Axis getAxis() {
        switch (this) {
            case DOWN:
            case UP: return Axis.Y;
            case NORTH:
            case SOUTH: return Axis.Z;
            default: return Axis.X;
        }
    }

    public static enum Axis {
        X, Y, Z;

        public boolean isHorizontal() {
            return this != Y;
        }
    }
}

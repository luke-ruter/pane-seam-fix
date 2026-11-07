package net.minecraft.world.level.block.state.properties;

// Compile-time stand-in mirroring Minecraft 26.3.
public abstract class Property<T extends Comparable<T>> {
    private final String name;

    protected Property(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}

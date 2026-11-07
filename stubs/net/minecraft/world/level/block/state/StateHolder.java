package net.minecraft.world.level.block.state;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.world.level.block.state.properties.Property;

// Compile-time stand-in mirroring Minecraft 26.3 (getValue / hasProperty live here).
public abstract class StateHolder<O, S> {
    protected final O owner;
    private final Map<Property<?>, Comparable<?>> values;

    protected StateHolder(O owner, Map<Property<?>, Comparable<?>> values) {
        this.owner = owner;
        this.values = new HashMap<>(values);
    }

    public boolean hasProperty(final Property<?> property) {
        return this.values.containsKey(property);
    }

    @SuppressWarnings("unchecked")
    public <T extends Comparable<T>> T getValue(final Property<T> property) {
        Comparable<?> value = this.values.get(property);
        if (value == null) {
            throw new IllegalArgumentException("Cannot get property " + property + " as it does not exist in " + this.owner);
        }
        return (T) value;
    }
}

package org.spongepowered.asm.mixin.injection.callback;

// Compile-time stand-in mirroring the real Mixin class.
public class CallbackInfo {
    private final String name;
    private final boolean cancellable;
    private boolean cancelled;

    public CallbackInfo(String name, boolean cancellable) {
        this.name = name;
        this.cancellable = cancellable;
    }

    public String getId() {
        return this.name;
    }

    public boolean isCancellable() {
        return this.cancellable;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void cancel() {
        if (!this.cancellable) {
            throw new IllegalStateException("not cancellable");
        }
        this.cancelled = true;
    }
}

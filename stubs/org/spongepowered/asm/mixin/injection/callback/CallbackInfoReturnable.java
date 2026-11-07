package org.spongepowered.asm.mixin.injection.callback;

// Compile-time stand-in mirroring the real Mixin class.
public class CallbackInfoReturnable<R> extends CallbackInfo {
    private R returnValue;

    public CallbackInfoReturnable(String name, boolean cancellable) {
        super(name, cancellable);
    }

    public void setReturnValue(R returnValue) {
        this.cancel();
        this.returnValue = returnValue;
    }

    public R getReturnValue() {
        return this.returnValue;
    }
}

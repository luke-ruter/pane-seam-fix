package paneseamfix.mixin;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paneseamfix.PaneSeamLogic;

/**
 * Hooks the face-culling check for full glass blocks. Clear, stained and tinted glass all inherit
 * {@code HalfTransparentBlock.skipRendering} without overriding it. Minecraft 26.x ships
 * unobfuscated, so these are the runtime names and no refmap is needed.
 */
@Mixin(value = HalfTransparentBlock.class, remap = false)
public abstract class HalfTransparentBlockMixin {
    @Inject(
            method = "skipRendering(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void paneseamfix$cullBetweenGlassBlocks(BlockState state, BlockState neighborState, Direction direction,
                                                    CallbackInfoReturnable<Boolean> cir) {
        if (PaneSeamLogic.shouldCullBetweenGlassBlocks(state, neighborState, direction)) {
            cir.setReturnValue(true);
        }
    }
}

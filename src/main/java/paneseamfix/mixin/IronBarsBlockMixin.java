package paneseamfix.mixin;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paneseamfix.PaneSeamLogic;

/**
 * Hooks the face-culling check that both vanilla's renderer and Sodium call
 * ({@code BlockState.skipRendering -> Block.skipRendering}). Minecraft 26.x ships unobfuscated,
 * so these are the runtime names and no refmap is needed.
 */
@Mixin(value = IronBarsBlock.class, remap = false)
public abstract class IronBarsBlockMixin {
    @Inject(
            method = "skipRendering(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void paneseamfix$cullBetweenGlassPanes(BlockState state, BlockState neighborState, Direction direction,
                                                   CallbackInfoReturnable<Boolean> cir) {
        if (PaneSeamLogic.shouldCullBetweenPanes(state, neighborState, direction)) {
            cir.setReturnValue(true);
        }
    }
}

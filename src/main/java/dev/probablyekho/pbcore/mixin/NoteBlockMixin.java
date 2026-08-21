package dev.probablyekho.pbcore.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NoteBlock.class)
public class NoteBlockMixin {
    @Redirect(method = "getStateForPlacement", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/NoteBlock;setInstrument(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/world/level/block/state/BlockState;"))
    private BlockState dontGetInstrument(NoteBlock instance, LevelAccessor noteblockinstrument2, BlockPos blockPos, BlockState state) {
        return state;
    }

    @Redirect(method = "updateShape", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/NoteBlock;setInstrument(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/world/level/block/state/BlockState;"))
    private BlockState dontUpdateInstrument(NoteBlock instance, LevelAccessor noteblockinstrument2, BlockPos blockPos, BlockState state) {
        return state;
    }

    @Unique
    private static final NoteBlockInstrument[] INSTRUMENTS = {
        NoteBlockInstrument.HARP,
        NoteBlockInstrument.BASEDRUM,
        NoteBlockInstrument.SNARE,
        NoteBlockInstrument.BASS,
        NoteBlockInstrument.HAT
    };

    @Unique
    private static NoteBlockInstrument nextInstrument(NoteBlockInstrument currentInstrument) {
        for (int i = 0; i < INSTRUMENTS.length; i++) {
            if (INSTRUMENTS[i] == currentInstrument) {
                return INSTRUMENTS[(i + 1) % INSTRUMENTS.length];
            }
        }

        return NoteBlockInstrument.HARP;
    }

    @Inject(method = "useWithoutItem", at = @At("HEAD"), cancellable = true)
    private void instrumentTune(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if(!player.isShiftKeyDown()) return;

        if(!level.isClientSide) {
            NoteBlockInstrument noteBlockInstrument = nextInstrument(state.getValue(NoteBlock.INSTRUMENT));

            BlockState newState = state.setValue(NoteBlock.INSTRUMENT, noteBlockInstrument);
            level.setBlock(pos, newState, Block.UPDATE_ALL);

            player.displayClientMessage(Component.literal("Set the instrument to: " + noteBlockInstrument.name().charAt(0) + noteBlockInstrument.name().substring(1).toLowerCase()), true);

            if (newState.getValue(NoteBlock.INSTRUMENT).worksAboveNoteBlock() || level.getBlockState(pos.above()).isAir()) {
                level.blockEvent(pos, newState.getBlock(), 0, 0);
                level.gameEvent(player, GameEvent.NOTE_BLOCK_PLAY, pos);
            }
        }

        cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide));
    }
}

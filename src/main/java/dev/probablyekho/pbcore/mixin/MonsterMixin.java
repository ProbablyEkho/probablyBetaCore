package dev.probablyekho.pbcore.mixin;

import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Monster;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Monster.class)
public class MonsterMixin {
    @Redirect(method = "checkMonsterSpawnRules", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/MobSpawnType;ignoresLightRequirements(Lnet/minecraft/world/entity/MobSpawnType;)Z"))
    private static boolean noSpawnerLightCheck(MobSpawnType spawnType) {
        if(spawnType == MobSpawnType.SPAWNER) {
            return true;
        }
        return MobSpawnType.ignoresLightRequirements(spawnType);
    }
}

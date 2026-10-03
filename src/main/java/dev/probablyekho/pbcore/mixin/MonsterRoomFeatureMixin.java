package dev.probablyekho.pbcore.mixin;

import com.mojang.serialization.Codec;
import dev.probablyekho.pbcore.Config;
import dev.probablyekho.pbcore.probablyBetaCore;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.MonsterRoomFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import java.util.function.Predicate;

@Mixin(MonsterRoomFeature.class)
public class MonsterRoomFeatureMixin extends Feature<NoneFeatureConfiguration> {
    public MonsterRoomFeatureMixin(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Unique private static final BlockState AIR = Blocks.CAVE_AIR.defaultBlockState();

    /**
     * @author ProbablyEkho
     * @reason Several stuffs   I don't feel like writing these comments lol
     */
    @Overwrite
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        Predicate<BlockState> predicate = Feature.isReplaceable(BlockTags.FEATURES_CANNOT_REPLACE);
        BlockPos blockpos = context.origin();
        RandomSource randomsource = context.random();
        WorldGenLevel worldgenlevel = context.level();
        int j = randomsource.nextInt(4) + 2;
        int k = -j - 1;
        int l = j + 1;
        int k1 = randomsource.nextInt(4) + 2;
        int l1 = -k1 - 1;
        int i2 = k1 + 1;
        int j2 = 0;
        int h = randomsource.nextInt(3) + 3;

        for (int k2 = k; k2 <= l; k2++) {
            for (int l2 = -1; l2 <= h; l2++) {
                for (int i3 = l1; i3 <= i2; i3++) {
                    BlockPos blockpos1 = blockpos.offset(k2, l2, i3);
                    boolean flag = worldgenlevel.getBlockState(blockpos1).isSolid();
                    if (l2 == -1 && !flag) {
                        return false;
                    }

                    if (l2 == h && !flag) {
                        return false;
                    }

                    if ((k2 == k || k2 == l || i3 == l1 || i3 == i2)
                        && l2 == 0
                        && worldgenlevel.isEmptyBlock(blockpos1)
                        && worldgenlevel.isEmptyBlock(blockpos1.above())) {
                        j2++;
                    }
                }
            }
        }

        if (j2 >= 1 && j2 <= 5) {
            for (int k3 = k; k3 <= l; k3++) {
                for (int i4 = h - 1; i4 >= -1; i4--) {
                    for (int k4 = l1; k4 <= i2; k4++) {
                        BlockPos blockpos3 = blockpos.offset(k3, i4, k4);
                        BlockState blockstate = worldgenlevel.getBlockState(blockpos3);
                        if (k3 == k || i4 == -1 || k4 == l1 || k3 == l || i4 == 4 || k4 == i2) {
                            if (blockpos3.getY() >= worldgenlevel.getMinBuildHeight() && !worldgenlevel.getBlockState(blockpos3.below()).isSolid()) {
                                worldgenlevel.setBlock(blockpos3, AIR, 2);
                            } else if (blockstate.isSolid() && !blockstate.is(Blocks.CHEST)) {
                                if (i4 == -1 && randomsource.nextInt(4) != 0) {
                                    this.safeSetBlock(worldgenlevel, blockpos3, Config.getDungeonMossyCobblestone(), predicate);
                                } else {
                                    this.safeSetBlock(worldgenlevel, blockpos3, Config.getDungeonCobblestone(), predicate);
                                }
                            }
                        } else if (!blockstate.is(Blocks.CHEST) && !blockstate.is(Blocks.SPAWNER)) {
                            this.safeSetBlock(worldgenlevel, blockpos3, AIR, predicate);
                        }
                    }
                }
            }

            for (int l3 = 0; l3 < 2; l3++) {
                for (int j4 = 0; j4 < 3; j4++) {
                    int l4 = blockpos.getX() + randomsource.nextInt(j * 2 + 1) - j;
                    int i5 = blockpos.getY();
                    int j5 = blockpos.getZ() + randomsource.nextInt(k1 * 2 + 1) - k1;
                    BlockPos blockpos2 = new BlockPos(l4, i5, j5);
                    if (worldgenlevel.isEmptyBlock(blockpos2)) {
                        int j3 = 0;

                        for (Direction direction : Direction.Plane.HORIZONTAL) {
                            if (worldgenlevel.getBlockState(blockpos2.relative(direction)).isSolid()) {
                                j3++;
                            }
                        }

                        if (j3 == 1) {
                            this.safeSetBlock(
                                worldgenlevel, blockpos2, StructurePiece.reorient(worldgenlevel, blockpos2, Blocks.CHEST.defaultBlockState()), predicate
                            );
                            RandomizableContainer.setBlockEntityLootTable(worldgenlevel, randomsource, blockpos2, BuiltInLootTables.SIMPLE_DUNGEON);
                            break;
                        }
                    }
                }
            }

            this.safeSetBlock(worldgenlevel, blockpos, Blocks.SPAWNER.defaultBlockState(), predicate);
            if (worldgenlevel.getBlockEntity(blockpos) instanceof SpawnerBlockEntity spawnerblockentity) {
                spawnerblockentity.setEntityId(net.neoforged.neoforge.common.MonsterRoomHooks.getRandomMonsterRoomMob(randomsource), randomsource);
            } else {
                probablyBetaCore.LOGGER.error("Failed to fetch mob spawner entity at ({}, {}, {})", blockpos.getX(), blockpos.getY(), blockpos.getZ());
            }

            return true;
        } else {
            return false;
        }
    }
}

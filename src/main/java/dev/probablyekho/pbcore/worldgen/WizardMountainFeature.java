package dev.probablyekho.pbcore.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.PushReaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WizardMountainFeature extends Feature<NoneFeatureConfiguration> {
    private static final int RADIUS = 25;
    private static final int HEIGHT = 30;
    private static final double DEPTH_SCALE = 1.25;
    private static final int MAX_VERTICAL_GAP = 3;
    private static final int DEPTH_OFFSET = 1;
    public WizardMountainFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }
    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        FlyingMountain flyingMountain = new FlyingMountain(context.level(), context.random(), context.origin().getX(), context.origin().getZ());
        flyingMountain.originSquare();
        for(int i = 0; i < RADIUS; i++) {
            flyingMountain.growAll();
        }
        return flyingMountain.create();
    }
    private static final int[][] adjacentSquares = {
            {-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}
    };
    private static long key(int x, int z) {
        return BlockPos.asLong(x, 0, z);
    }
    private static class Square {
        public final int x;
        public int y;
        public final int z;
        public final double radius;
        public double weight = -1.0;
        public double growth = 0.0;
        public int adjacent = 0;
        public boolean isLog;
        private Square(WorldGenLevel level, int x, int z, int originX, int originZ) {
            this.x = x;
            this.z = z;
            this.y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
            int deltaX = Math.abs(x - originX);
            int deltaZ = Math.abs(z - originZ);
            this.radius = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
            this.isLog = level.getBlockState(new BlockPos(x, this.y - 1, z)).is(BlockTags.LOGS);
        }
        private boolean grow() {
            double newWeight = this.weight + this.growth;
            boolean reachedZero = newWeight >= 0.0 && this.weight < 0.0;
            this.weight = newWeight;
            return reachedZero;
        }
    }
    private static class FlyingMountain {
        public final WorldGenLevel level;
        public final RandomSource randomSource;
        public final int originX;
        public final int originZ;
        public final Map<Long, Square> interior = new HashMap<>();
        public final Map<Long, Square> outside = new HashMap<>();
        public Square origin;
        private FlyingMountain(WorldGenLevel level, RandomSource randomSource, int originX, int originZ) {
            this.level = level;
            this.randomSource = randomSource;
            this.originX = originX;
            this.originZ = originZ;
        }
        private void originSquare() {
            long squareKey = key(originX, originZ);
            Square square = interior.get(squareKey);
            if(square == null) {
                square = outside.get(squareKey);
            }
            if(square == null) {
                square = new Square(level, originX, originZ, originX, originZ);
                outside.put(squareKey, square);
            }
            square.weight = -0.1;
            square.growth = 0.11;
            growSquare(square);
            this.origin = square;
        }
        private void addAdjacent(Square center) {
            int x = center.x;
            int y = center.y;
            int z = center.z;
            for(int[] offset : adjacentSquares) {
                int curX = x + offset[0];
                int curZ = z + offset[1];
                long squareKey = key(curX, curZ);
                Square square = interior.get(squareKey);
                if(square == null) {
                    square = outside.get(squareKey);
                }
                if(square == null) {
                    square = new Square(level, curX, curZ, originX, originZ);
                    outside.put(squareKey, square);
                }
                square.adjacent++;
                if(square.adjacent == 6) {
                    square.growth += 1;
                }
                if(square.isLog) {
                    square.y = y;
                }
                int heightDiff = Math.abs(y - square.y);
                double addedGrowth;
                if(heightDiff == 0) {
                    addedGrowth = 0.4714;
                } else if(heightDiff > MAX_VERTICAL_GAP) {
                    addedGrowth = 0;
                } else {
                    addedGrowth = (1 + MAX_VERTICAL_GAP - heightDiff) / (1 + MAX_VERTICAL_GAP * 4.5);
                }
                if(x != curX && z != curZ) {
                    addedGrowth /= 1.41421;
                }
                square.growth += addedGrowth;
            }
        }
        private void growSquare(Square square) {
            if(square.grow() && square.radius <= RADIUS) {
                long squareKey = key(square.x, square.z);
                interior.put(squareKey, square);
                outside.remove(squareKey);
                addAdjacent(square);
            }
        }
        private void growAll() {
            for(Square square : new ArrayList<>(outside.values())) {
                growSquare(square);
            }
            for(Square square : interior.values()) {
                square.grow();
            }
        }
        private boolean create() {
            if(interior.isEmpty()) {
                return false;
            }
            double depthScale = 6 / origin.weight;
            for(Square square : interior.values()) {
                int y = square.y;
                int thisDepth = (int) (DEPTH_SCALE * ((depthScale * square.weight * (RADIUS - square.radius)/RADIUS ) +0.1 + randomSource.nextDouble() * 3));
                thisDepth += 2 + DEPTH_OFFSET;
                int startY = y - thisDepth;
                if(startY < level.getMinBuildHeight()) {
                    startY = level.getMinBuildHeight();
                }
                int endY = level.getHeight(Heightmap.Types.WORLD_SURFACE, square.x, square.z);
                for(int j = endY; j > startY; j--) {
                    int newY = j + HEIGHT;
                    BlockPos blockPos = new BlockPos(square.x, j, square.z);
                    if(newY > level.getMaxBuildHeight()) {
                        level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
                        continue;
                    }
                    BlockState blockState = level.getBlockState(blockPos);
                    if(blockState.getPistonPushReaction() != PushReaction.BLOCK) {
                        level.setBlock(new BlockPos(square.x, newY, square.z), blockState, 2);
                        level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
                    }
                }
            }
            return true;
        }
    }
}

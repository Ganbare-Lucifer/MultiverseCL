package io.github.davidqf555.minecraft.multiverse.common.world.rifts;

import io.github.davidqf555.minecraft.multiverse.common.RegistryHandler;
import io.github.davidqf555.minecraft.multiverse.common.ServerConfigs;
import io.github.davidqf555.minecraft.multiverse.common.blocks.RiftBlock;
import io.github.davidqf555.minecraft.multiverse.common.blocks.RiftTileEntity;
import io.github.davidqf555.minecraft.multiverse.common.world.DimensionHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.common.util.Constants;

import java.util.Optional;
import java.util.Random;

public class RiftFeature extends Feature<RiftConfig> {

    public static final RiftFeature INSTANCE = new RiftFeature();
    public static final ConfiguredFeature<?, ?> CONFIG = new ConfiguredFeature<>(INSTANCE, RiftConfig.of(Optional.empty(), RegistryHandler.RIFT_BLOCK.get().defaultBlockState().setValue(RiftBlock.TEMPORARY, false), true)).decorated(RiftPlacement.CONFIG).chance(ServerConfigs.INSTANCE.riftChance.get());

    public RiftFeature() {
        super(RiftConfig.CODEC);
    }

    @Override
    public boolean place(ISeedReader reader, ChunkGenerator gen, Random rand, BlockPos center, RiftConfig config) {
        int target = config.getTarget().orElseGet(() -> {
            int current = DimensionHelper.getIndex(reader.getLevel().dimension());
            int world = rand.nextInt(ServerConfigs.INSTANCE.maxDimensions.get());
            return world < current ? world : world + 1;
        });
        Block block = RegistryHandler.RIFT_BLOCK.get();
        BlockState rift = config.getBlockState();
        BlockState air = Blocks.AIR.defaultBlockState();
        boolean natural = config.isNatural();
        if (!natural) {
            reader.getLevel().levelEvent(Constants.WorldEvents.GATEWAY_SPAWN_EFFECTS, center, 0);
        }
        int totalWidth = config.getWidth(rand);
        int totalHeight = config.getHeight(rand);
        float xRot = config.getRotX(rand) * (float) Math.PI / 180;
        float yRot = config.getRotY(rand) * (float) Math.PI / 180;
        float zRot = config.getRotZ(rand) * (float) Math.PI / 180;
        Vector3d centerVec = Vector3d.atCenterOf(center);
        for (int y = -totalHeight; y <= totalHeight; y++) {
            int width = totalWidth * (totalHeight - MathHelper.abs(y)) / totalHeight;
            for (int x = -width; x <= width; x++) {
                Vector3d vec = applyRotation(new Vector3d(x, y, 0), xRot, yRot, zRot);
                BlockPos pos = new BlockPos(centerVec.add(vec));
                if (canReplace(reader, pos)) {
                    if (!natural) {
                        reader.destroyBlock(pos, true);
                    }
                    setBlock(reader, pos, rift);
                    TileEntity tile = reader.getBlockEntity(pos);
                    if (tile instanceof RiftTileEntity) {
                        ((RiftTileEntity) tile).setTarget(target);
                    }
                }
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        for (int k = -1; k <= 1; k++) {
                            BlockPos replace = pos.offset(i, j, k);
                            BlockState state = reader.getBlockState(replace);
                            if (canReplace(reader, replace) && !state.getBlock().equals(block)) {
                                if (natural) {
                                    setBlock(reader, replace, air);
                                } else {
                                    reader.destroyBlock(replace, true);
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    private Vector3d applyRotation(Vector3d vec, float xRot, float yRot, float zRot) {
        double cos = MathHelper.cos(zRot);
        double sin = MathHelper.sin(zRot);
        double x = vec.x() * cos + vec.y() * sin;
        double y = vec.y() * cos - vec.x() * sin;
        return new Vector3d(x, y, vec.z()).xRot(xRot).yRot(yRot);
    }

    private boolean canReplace(ISeedReader reader, BlockPos pos) {
        int blockY = pos.getY();
        return blockY >= 0 && blockY < reader.getMaxBuildHeight() && reader.getBlockState(pos).getDestroySpeed(reader, pos) != -1;
    }

}
package io.github.davidqf555.minecraft.multiverse.common.items.tools;

import io.github.davidqf555.minecraft.multiverse.common.Multiverse;
import io.github.davidqf555.minecraft.multiverse.common.ServerConfigs;
import io.github.davidqf555.minecraft.multiverse.common.packets.RiftParticlesPacket;
import io.github.davidqf555.minecraft.multiverse.common.worldgen.DimensionHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

public final class MultiversalToolHelper {

    public static final Component LORE = Component.translatable(Util.makeDescriptionId("item", new ResourceLocation(Multiverse.MOD_ID, "multiversal_lore"))).withStyle(ChatFormatting.LIGHT_PURPLE);
    public static final Component INSTRUCTIONS = Component.translatable(Util.makeDescriptionId("item", new ResourceLocation(Multiverse.MOD_ID, "multiversal_instructions"))).withStyle(ChatFormatting.BLUE);

    private MultiversalToolHelper() {
    }

    public static int getTarget(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTagElement(Multiverse.MOD_ID);
        return tag.contains("Target", Tag.TAG_INT) ? tag.getInt("Target") : 0;
    }

    public static void setTarget(ItemStack stack, int target) {
        CompoundTag tag = stack.getOrCreateTagElement(Multiverse.MOD_ID);
        tag.putInt("Target", target);
    }

    public static void setRandomTarget(ServerLevel world, ItemStack stack) {
        int current = getTarget(stack);
        List<Integer> existing = new ArrayList<>();
        int max = ServerConfigs.INSTANCE.maxDimensions.get();
        for (int i = 0; i <= max; i++) {
            if (DimensionHelper.getWorld(world.getServer(), i).isPresent()) {
                existing.add(i);
            }
        }
        existing.remove(Integer.valueOf(current));
        setTarget(stack, existing.isEmpty() ? 0 : existing.get(world.getRandom().nextInt(existing.size())));
    }

    public static void setCurrent(Level world, ItemStack stack) {
        setTarget(stack, DimensionHelper.getIndex(world.dimension()));
    }

    public static void mineBlock(Player entity, ServerLevel world, ItemStack stack, BlockPos pos) {
        int target = MultiversalToolHelper.getTarget(stack);
        int current = DimensionHelper.getIndex(world.dimension());
        if (target != current) {
            DimensionHelper.getWorld(world.getServer(), target).ifPresent(w -> {
                double scale = DimensionType.getTeleportationScale(w.dimensionType(), world.dimensionType());
                BlockPos block = BlockPos.containing(pos.getX() * scale, pos.getY(), pos.getZ() * scale);
                BlockState s = w.getBlockState(block);
                if (isBreakable(w, s, block) && w.destroyBlock(block, false, entity)) {
                    Multiverse.CHANNEL.send(PacketDistributor.DIMENSION.with(w::dimension), new RiftParticlesPacket(OptionalInt.of(current), Vec3.atCenterOf(block)));
                    Multiverse.CHANNEL.send(PacketDistributor.DIMENSION.with(world::dimension), new RiftParticlesPacket(OptionalInt.of(target), Vec3.atCenterOf(pos)));
                    Block.dropResources(s, new LootContext.Builder(world)
                            .withRandom(entity.getRandom())
                            .withParameter(LootContextParams.TOOL, stack)
                            .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                            .withParameter(LootContextParams.BLOCK_STATE, s)
                            .withOptionalParameter(LootContextParams.BLOCK_ENTITY, w.getBlockEntity(block))
                            .withParameter(LootContextParams.THIS_ENTITY, entity)
                    );
                }
            });
        }
    }

    private static boolean isBreakable(Level world, BlockState state, BlockPos pos) {
        return !state.isAir() && state.getFluidState().isEmpty() && state.getDestroySpeed(world, pos) != -1;
    }

}
package io.github.davidqf555.minecraft.multiverse.common.events;

import io.github.davidqf555.minecraft.multiverse.common.ArrowSummonsData;
import io.github.davidqf555.minecraft.multiverse.common.Multiverse;
import io.github.davidqf555.minecraft.multiverse.common.items.IDeathEffect;
import io.github.davidqf555.minecraft.multiverse.common.worldgen.ShapesManager;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@Mod.EventBusSubscriber(modid = Multiverse.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ForgeBus {

    private ForgeBus() {
    }

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        ShapesManager.INSTANCE.load(event.getServer());
    }

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.START && !event.level.isClientSide()) {
            ArrowSummonsData.get((ServerLevel) event.level).ifPresent(data -> data.tick((ServerLevel) event.level));
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        ItemStack main = entity.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack off = entity.getItemInHand(InteractionHand.OFF_HAND);
        if (!main.isEmpty() && main.getItem() instanceof IDeathEffect) {
            if (((IDeathEffect) main.getItem()).onDeath(entity, main)) {
                event.setCanceled(true);
            }
            main.split(1);
            return;
        }
        if (!off.isEmpty() && off.getItem() instanceof IDeathEffect) {
            if (((IDeathEffect) off.getItem()).onDeath(entity, off)) {
                event.setCanceled(true);
            }
            off.split(1);
        }
    }

}

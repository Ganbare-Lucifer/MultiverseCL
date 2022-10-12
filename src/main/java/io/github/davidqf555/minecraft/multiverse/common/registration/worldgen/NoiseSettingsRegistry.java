package io.github.davidqf555.minecraft.multiverse.common.registration.worldgen;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.davidqf555.minecraft.multiverse.common.Multiverse;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.TerrainProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.*;

import java.util.Map;
import java.util.function.Function;

public final class NoiseSettingsRegistry {

    public static final Map<ResourceKey<NoiseGeneratorSettings>, NoiseGeneratorSettings> ALL;
    public static final ResourceKey<NoiseGeneratorSettings> TOP = ResourceKey.create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, new ResourceLocation(Multiverse.MOD_ID, "top"));
    public static final ResourceKey<NoiseGeneratorSettings> BOTTOM = ResourceKey.create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, new ResourceLocation(Multiverse.MOD_ID, "bottom"));
    public static final ResourceKey<NoiseGeneratorSettings> NONE = ResourceKey.create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, new ResourceLocation(Multiverse.MOD_ID, "none"));
    public static final ResourceKey<NoiseGeneratorSettings> BOTH = ResourceKey.create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, new ResourceLocation(Multiverse.MOD_ID, "both"));

    static {
        ImmutableMap.Builder<ResourceKey<NoiseGeneratorSettings>, NoiseGeneratorSettings> builder = ImmutableMap.builder();
        builder.put(TOP, createDefaultSettings(false, true, NoiseSettings.create(0, 128, new NoiseSamplingSettings(1, 1, 80, 30), new NoiseSlider(120, 3, 0), new NoiseSlider(-30, 7, 1), 1, 2, TerrainProvider.nether()), NoiseRouterData::nether, 0));
        builder.put(BOTTOM, createDefaultSettings(true, false, NoiseSettings.create(0, 256, new NoiseSamplingSettings(1, 2, 80, 160), new NoiseSlider(-10, 3, 0), new NoiseSlider(-30, 0, 0), 1, 2, TerrainProvider.overworld(false)), noise -> NoiseRouterData.overworldWithNewCaves(noise, false), 63));
        builder.put(NONE, createDefaultSettings(false, false, NoiseSettings.create(0, 128, new NoiseSamplingSettings(2, 1, 80, 160), new NoiseSlider(-3000, 64, -46), new NoiseSlider(-30, 7, 1), 2, 1, TerrainProvider.end()), NoiseRouterData::end, 0));
        builder.put(BOTH, createDefaultSettings(true, true, NoiseSettings.create(0, 128, new NoiseSamplingSettings(1, 3, 80, 60), new NoiseSlider(120, 3, 0), new NoiseSlider(320, 4, -1), 1, 2, TerrainProvider.nether()), NoiseRouterData::nether, 32));
        ALL = builder.build();
    }

    private NoiseSettingsRegistry() {
    }

    //needs work
    private static NoiseGeneratorSettings createDefaultSettings(boolean floor, boolean ceiling, NoiseSettings noise, Function<NoiseSettings, NoiseRouterWithOnlyNoises> router, int sea) {
        return new NoiseGeneratorSettings(noise, Blocks.STONE.defaultBlockState(), Blocks.WATER.defaultBlockState(), router.apply(noise), getDefaultSurfaceRule(floor, ceiling), sea, false, true, true, false);
    }

    private static SurfaceRules.RuleSource getDefaultSurfaceRule(boolean floor, boolean ceiling) {
        return getBedrockSurfaceRule(floor, ceiling);
    }

    private static SurfaceRules.RuleSource getBedrockSurfaceRule(boolean floor, boolean ceiling) {
        if (!floor && !ceiling) {
            return SurfaceRules.state(Blocks.AIR.defaultBlockState());
        }
        SurfaceRules.RuleSource bedrock = SurfaceRules.state(Blocks.BEDROCK.defaultBlockState());
        ImmutableList.Builder<SurfaceRules.RuleSource> builder = ImmutableList.builder();
        if (floor) {
            builder.add(SurfaceRules.ifTrue(SurfaceRules.verticalGradient("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)), bedrock));
        }
        if (ceiling) {
            builder.add(SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.verticalGradient("bedrock_roof", VerticalAnchor.belowTop(5), VerticalAnchor.top())), bedrock));
        }
        return SurfaceRules.sequence(builder.build().toArray(SurfaceRules.RuleSource[]::new));
    }

}
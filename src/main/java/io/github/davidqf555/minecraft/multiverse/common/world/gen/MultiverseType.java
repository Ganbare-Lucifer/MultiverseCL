package io.github.davidqf555.minecraft.multiverse.common.world.gen;

import io.github.davidqf555.minecraft.multiverse.common.registration.worldgen.NoiseSettingsRegistry;
import net.minecraft.data.worldgen.SurfaceRuleData;
import net.minecraft.data.worldgen.TerrainProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.TerrainShaper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.*;
import net.minecraftforge.common.BiomeDictionary;

import java.util.function.Predicate;

public enum MultiverseType {

    OVERWORLD(10, NoiseSettingsRegistry.OVERWORLD, false, true, -64, 384, Blocks.STONE.defaultBlockState(), Blocks.WATER.defaultBlockState(), new NoiseSamplingSettings(1, 1, 80, 160), new NoiseSlider(-0.078125D, 2, 8), new NoiseSlider(0.1171875, 3, 0), 1, 2, TerrainProvider.overworld(false), 63, SurfaceRuleData.overworldLike(true, false, true), type -> !type.equals(BiomeDictionary.Type.NETHER) && !type.equals(BiomeDictionary.Type.END), biome -> BiomeDictionary.hasType(biome, BiomeDictionary.Type.OVERWORLD), Biomes.PLAINS),
    OVERWORLD_ISLANDS(10, NoiseSettingsRegistry.OVERWORLD_ISLANDS, false, false, 0, 256, Blocks.STONE.defaultBlockState(), Blocks.WATER.defaultBlockState(), new NoiseSamplingSettings(2, 1, 80, 160), new NoiseSlider(-23.4375, 64, -46), new NoiseSlider(-0.234375, 7, 1), 2, 1, TerrainProvider.floatingIslands(), -64, SurfaceRuleData.overworldLike(false, false, true), type -> !type.equals(BiomeDictionary.Type.NETHER) && !type.equals(BiomeDictionary.Type.END), biome -> BiomeDictionary.hasType(biome, BiomeDictionary.Type.OVERWORLD), Biomes.PLAINS),
    NETHER(5, NoiseSettingsRegistry.NETHER, false, true, -64, 384, Blocks.NETHERRACK.defaultBlockState(), Blocks.LAVA.defaultBlockState(), new NoiseSamplingSettings(1, 1, 80, 160), new NoiseSlider(-0.078125D, 2, 8), new NoiseSlider(0.1171875, 3, 0), 1, 2, TerrainProvider.overworld(false), 63, MultiverseSurfaceRuleData.nether(false, true), type -> !type.equals(BiomeDictionary.Type.OVERWORLD) && !type.equals(BiomeDictionary.Type.END), biome -> BiomeDictionary.hasType(biome, BiomeDictionary.Type.NETHER), Biomes.NETHER_WASTES),
    NETHER_ROOFED(5, NoiseSettingsRegistry.NETHER_ROOFED, true, true, 0, 128, Blocks.NETHERRACK.defaultBlockState(), Blocks.LAVA.defaultBlockState(), new NoiseSamplingSettings(1, 3, 80, 60), new NoiseSlider(0.9375, 3, 0), new NoiseSlider(2.5, 4, -1), 1, 2, TerrainProvider.nether(), 32, MultiverseSurfaceRuleData.nether(true, true), type -> !type.equals(BiomeDictionary.Type.OVERWORLD) && !type.equals(BiomeDictionary.Type.END), biome -> BiomeDictionary.hasType(biome, BiomeDictionary.Type.NETHER), Biomes.NETHER_WASTES),
    NETHER_ISLANDS(5, NoiseSettingsRegistry.NETHER_ISLANDS, false, false, 0, 256, Blocks.NETHERRACK.defaultBlockState(), Blocks.LAVA.defaultBlockState(), new NoiseSamplingSettings(2, 1, 80, 160), new NoiseSlider(-23.4375, 64, -46), new NoiseSlider(-0.234375, 7, 1), 2, 1, TerrainProvider.floatingIslands(), -64, MultiverseSurfaceRuleData.nether(false, false), type -> !type.equals(BiomeDictionary.Type.OVERWORLD) && !type.equals(BiomeDictionary.Type.END), biome -> BiomeDictionary.hasType(biome, BiomeDictionary.Type.NETHER), Biomes.NETHER_WASTES),
    END(3, NoiseSettingsRegistry.END, false, true, -64, 384, Blocks.END_STONE.defaultBlockState(), Blocks.AIR.defaultBlockState(), new NoiseSamplingSettings(1, 1, 80, 160), new NoiseSlider(-0.078125D, 2, 8), new NoiseSlider(0.1171875, 3, 0), 1, 2, TerrainProvider.overworld(false), 63, MultiverseSurfaceRuleData.end(false, true), type -> !type.equals(BiomeDictionary.Type.OVERWORLD) && !type.equals(BiomeDictionary.Type.NETHER), biome -> BiomeDictionary.hasType(biome, BiomeDictionary.Type.END), Biomes.THE_END),
    END_ROOFED(3, NoiseSettingsRegistry.END_ROOFED, true, true, 0, 128, Blocks.END_STONE.defaultBlockState(), Blocks.AIR.defaultBlockState(), new NoiseSamplingSettings(1, 3, 80, 60), new NoiseSlider(0.9375, 3, 0), new NoiseSlider(2.5, 4, -1), 1, 2, TerrainProvider.nether(), 32, MultiverseSurfaceRuleData.end(false, true), type -> !type.equals(BiomeDictionary.Type.OVERWORLD) && !type.equals(BiomeDictionary.Type.NETHER), biome -> BiomeDictionary.hasType(biome, BiomeDictionary.Type.END), Biomes.THE_END),
    END_ISLANDS(3, NoiseSettingsRegistry.END_ISLANDS, false, false, 0, 256, Blocks.END_STONE.defaultBlockState(), Blocks.AIR.defaultBlockState(), new NoiseSamplingSettings(2, 1, 80, 160), new NoiseSlider(-23.4375, 64, -46), new NoiseSlider(-0.234375, 7, 1), 2, 1, TerrainProvider.floatingIslands(), -64, MultiverseSurfaceRuleData.end(false, true), type -> !type.equals(BiomeDictionary.Type.OVERWORLD) && !type.equals(BiomeDictionary.Type.NETHER), biome -> BiomeDictionary.hasType(biome, BiomeDictionary.Type.END), Biomes.THE_END);

    private final ResourceKey<NoiseGeneratorSettings> key;
    private final NoiseGeneratorSettings settings;
    private final boolean floor, ceiling;
    private final int height, weight, minY;
    private final Predicate<BiomeDictionary.Type> biomeType;
    private final Predicate<ResourceKey<Biome>> biome;
    private final ResourceKey<Biome> base;

    MultiverseType(int weight, ResourceKey<NoiseGeneratorSettings> key, boolean ceiling, boolean floor, int minY, int height, BlockState defBlock, BlockState defFluid, NoiseSamplingSettings sampling, NoiseSlider top, NoiseSlider bottom, int sizeHorizontal, int sizeVertical, TerrainShaper terrain, int sea, SurfaceRules.RuleSource surface, Predicate<BiomeDictionary.Type> biomeType, Predicate<ResourceKey<Biome>> biome, ResourceKey<Biome> base) {
        this.weight = weight;
        this.minY = minY;
        this.key = key;
        this.floor = floor;
        this.ceiling = ceiling;
        this.height = height;
        this.biomeType = biomeType;
        this.biome = biome;
        this.base = base;
        NoiseSettings noise = NoiseSettings.create(this.minY, this.height, sampling, top, bottom, sizeHorizontal, sizeVertical, terrain);
        this.settings = new NoiseGeneratorSettings(noise, defBlock, defFluid, !ceiling && floor ? NoiseRouterData.overworldWithNewCaves(noise, false) : NoiseRouterData.nether(noise), surface, sea, false, true, true, false);
    }

    public int getMinY() {
        return minY;
    }

    public ResourceKey<NoiseGeneratorSettings> getNoiseSettingsKey() {
        return key;
    }

    public NoiseGeneratorSettings getNoiseSettings() {
        return settings;
    }

    public boolean hasFloor() {
        return floor;
    }

    public boolean hasCeiling() {
        return ceiling;
    }

    public int getHeight() {
        return height;
    }

    public boolean isValidType(BiomeDictionary.Type type) {
        return biomeType.test(type);
    }

    public boolean isValidBiome(ResourceKey<Biome> biome) {
        return this.biome.test(biome);
    }

    public ResourceKey<Biome> getDefaultBiome() {
        return base;
    }

    public int getWeight() {
        return weight;
    }

}

package io.github.davidqf555.minecraft.multiverse.common.worldgen.biomes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import io.github.davidqf555.minecraft.multiverse.common.Multiverse;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.GsonHelper;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class BiomeTypesManager {

    public static final BiomeTypesManager INSTANCE = new BiomeTypesManager(new ResourceLocation(Multiverse.MOD_ID, "biome_types.json"));
    private static final Gson GSON = new GsonBuilder().create();
    private static final Logger LOGGER = LogUtils.getLogger();
    private final Set<BiomeType> biomes = new HashSet<>();
    private final ResourceLocation loc;

    protected BiomeTypesManager(ResourceLocation loc) {
        this.loc = loc;
    }

    public Set<BiomeType> getBiomeTypes() {
        return biomes;
    }

    public void load(MinecraftServer server) {
        Resource resource;
        try {
            resource = server.getResourceManager().getResource(loc);
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage());
        }
        Reader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));

        JsonArray values = GsonHelper.fromJson(GSON, reader, JsonElement.class).getAsJsonObject().getAsJsonArray("types");
        RegistryOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, server.registryAccess());
        biomes.clear();
        for (JsonElement type : values) {
            BiomeType.CODEC.decode(ops, type).resultOrPartial(LOGGER::error).map(Pair::getFirst).ifPresent(biomes::add);
        }
        if (biomes.isEmpty()) {
            throw new IllegalStateException("There cannot be 0 biome types");
        }
        if (biomes.stream().mapToInt(BiomeType::getWeight).sum() <= 0) {
            throw new IllegalStateException("Total weight must be greater than 0");
        }
    }

}

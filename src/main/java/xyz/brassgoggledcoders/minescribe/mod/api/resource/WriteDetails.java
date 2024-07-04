package xyz.brassgoggledcoders.minescribe.mod.api.resource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import org.slf4j.Logger;
import xyz.brassgoggledcoders.minescribe.mod.api.MineScribeAPI;
import xyz.brassgoggledcoders.minescribe.mod.api.models.MineScribePackType;

import java.util.function.Supplier;

public record WriteDetails(
        ResourceLocation writeLocation,
        PackType packType,
        Supplier<String> contents
) {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public static <T extends U, U> WriteDetails create(ResourceLocation writeLocation, T value, Codec<U> writer) {
        return new WriteDetails(
                writeLocation,
                MineScribeAPI.MINESCRIBE_PACK_TYPE,
                () -> GSON.toJson(writer.encodeStart(JsonOps.INSTANCE, value)
                        .getOrThrow(false, string -> LOGGER.error("Failed to create WriteDetails: {}", string)))
        );
    }
}

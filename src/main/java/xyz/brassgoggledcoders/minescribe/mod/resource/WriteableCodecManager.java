package xyz.brassgoggledcoders.minescribe.mod.resource;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import xyz.brassgoggledcoders.minescribe.mod.api.resource.IWriteableManager;
import xyz.brassgoggledcoders.minescribe.mod.api.resource.WriteDetails;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class WriteableCodecManager<T> extends SimpleJsonResourceReloadListener implements IWriteableManager {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final Map<ResourceLocation, T> values;

    private final Codec<T> codec;

    public WriteableCodecManager(String directory, Codec<T> codec) {
        super(new Gson(), directory);
        this.codec = codec;

        this.values = new HashMap<>();
    }

    @Override
    public Stream<WriteDetails> getWriteDetails() {
        return this.values.entrySet()
                .parallelStream()
                .map(entry -> WriteDetails.create(
                        this.getPreparedPath(entry.getKey()),
                        entry.getValue(),
                        codec
                ));
    }

    @Override
    protected void apply(
            @NotNull Map<ResourceLocation, JsonElement> jsonValues,
            @NotNull ResourceManager resourceManager,
            @NotNull ProfilerFiller profilerFiller
    ) {
        for (Map.Entry<ResourceLocation, JsonElement> entry : jsonValues.entrySet()) {
            T value = this.codec.decode(JsonOps.INSTANCE, entry.getValue())
                    .get()
                    .map(
                            Pair::getFirst,
                            partial -> {
                                LOGGER.error("Failed to read json {} for {} due to {}", entry.getValue(), entry.getKey(), partial.message());
                                return null;
                            }
                    );

            if (value != null) {
                this.values.put(entry.getKey(), value);
            }
        }
    }
}

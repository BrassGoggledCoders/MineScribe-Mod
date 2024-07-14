package xyz.brassgoggledcoders.minescribe.mod.resource;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import xyz.brassgoggledcoders.minescribe.mod.api.resource.IWriteableManager;
import xyz.brassgoggledcoders.minescribe.mod.api.resource.WriteDetails;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class CollectingWritingManager<T, V> implements IWriteableManager {

    private final String directory;
    private final Supplier<Stream<Pair<ResourceLocation, T>>> supplier;
    private final Function<T, V> mapper;
    private final Codec<V> writer;

    public CollectingWritingManager(String directory, Supplier<Stream<Pair<ResourceLocation, T>>> supplier,
                                    Function<T, V> mapper, Codec<V> writer) {
        this.directory = directory;
        this.supplier = supplier;
        this.mapper = mapper;
        this.writer = writer;
    }

    @Override
    public Stream<WriteDetails> getWriteDetails() {
        return this.supplier.get()
                .map(pair -> Pair.of(
                        pair.getFirst(),
                        this.mapper.apply(pair.getSecond())
                ))
                .map(pair -> WriteDetails.create(
                        this.getPreparedPath(pair.getFirst()),
                        pair.getSecond(),
                        this.writer
                ));
    }

    private ResourceLocation getPreparedPath(ResourceLocation rl) {
        return rl.withPath(this.directory + "/" + rl.getPath() + ".json");
    }
}

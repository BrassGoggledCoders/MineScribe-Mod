package xyz.brassgoggledcoders.minescribe.mod.api.models;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

public record MineScribePath(
        String anchor,
        String path
) {
    public static final Codec<MineScribePath> CODEC = ExtraCodecs.withAlternative(
            RecordCodecBuilder.create(instance -> instance.group(
                    Codec.STRING.fieldOf("anchor").forGetter(MineScribePath::anchor),
                    Codec.STRING.fieldOf("path").forGetter(MineScribePath::path)
            ).apply(instance, MineScribePath::new)),
            Codec.STRING,
            path -> new MineScribePath(
                    "ROOT",
                    path
            )
    );
}

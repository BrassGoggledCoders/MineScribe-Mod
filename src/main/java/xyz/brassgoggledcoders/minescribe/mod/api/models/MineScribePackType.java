package xyz.brassgoggledcoders.minescribe.mod.api.models;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;

public record MineScribePackType(
        Component label,
        String folder
) {
    public static final Codec<MineScribePackType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ComponentSerialization.CODEC.fieldOf("label")
                    .forGetter(MineScribePackType::label),
            Codec.STRING.fieldOf("folder")
                    .xmap(String::toLowerCase, String::toLowerCase)
                    .forGetter(MineScribePackType::folder)
    ).apply(instance, MineScribePackType::new));
}

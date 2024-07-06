package xyz.brassgoggledcoders.minescribe.mod.api.models;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record PackRepository(
        Component label,
        MineScribePath path,
        List<ResourceLocation> packTypes
) {
    public static final Codec<PackRepository> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ComponentSerialization.CODEC.fieldOf("label")
                    .forGetter(PackRepository::label),
            MineScribePath.CODEC.fieldOf("path")
                    .forGetter(PackRepository::path),
            ResourceLocation.CODEC.listOf()
                    .fieldOf("packTypes")
                    .forGetter(PackRepository::packTypes)
    ).apply(instance, PackRepository::new));
}

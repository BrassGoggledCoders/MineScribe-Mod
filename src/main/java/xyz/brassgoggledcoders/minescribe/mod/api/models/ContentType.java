package xyz.brassgoggledcoders.minescribe.mod.api.models;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Optional;

public record ContentType(
        Component label,
        Optional<ResourceLocation> category,
        MineScribePath path,
        List<ResourceLocation> packTypes
) {
    public static final Codec<ContentType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ComponentSerialization.CODEC.fieldOf("label").forGetter(ContentType::label),
            ResourceLocation.CODEC.optionalFieldOf("category").forGetter(ContentType::category),
            MineScribePath.CODEC.fieldOf("path").forGetter(ContentType::path),
            ResourceLocation.CODEC.listOf().fieldOf("packTypes").forGetter(ContentType::packTypes)
    ).apply(instance, instance.stable(ContentType::new)));
}

package xyz.brassgoggledcoders.minescribe.mod.api.models;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;

public record ContentCategory(
        Component label
) {
    public static final Codec<ContentCategory> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ComponentSerialization.CODEC.fieldOf("label").forGetter(ContentCategory::label)
    ).apply(instance, instance.stable(ContentCategory::new)));
}

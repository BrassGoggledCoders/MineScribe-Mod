package xyz.brassgoggledcoders.minescribe.mod.api.resource;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MineScribeConditionContext implements ICondition.IContext {
    private final Map<ResourceKey<?>, Map<ResourceLocation, Collection<Holder<?>>>> loadedTags;
    private final MinecraftServer server;

    public MineScribeConditionContext(MinecraftServer server) {
        this.server = server;

        this.loadedTags = new HashMap<>();
    }

    @Override
    @NotNull
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T> Map<ResourceLocation, Collection<Holder<T>>> getAllTags(@NotNull ResourceKey<? extends Registry<T>> registryKey) {
        return (Map<ResourceLocation, Collection<Holder<T>>>)(Map)this.loadedTags.computeIfAbsent(
                registryKey,
                value -> (Map<ResourceLocation, Collection<Holder<?>>>)(Map)this.loadTagsFor(registryKey)
        );
    }

    private <T> Map<ResourceLocation, Collection<Holder<T>>> loadTagsFor(ResourceKey<? extends Registry<T>> registryKey) {
        return this.server.registryAccess()
                .lookup(registryKey)
                .map(registry -> registry.listTags()
                        .collect(Collectors.<HolderSet.Named<T>, ResourceLocation, Collection<Holder<T>>>toMap(
                                named -> named.key()
                                        .location(),
                                named -> named.stream()
                                        .toList()
                        ))
                )
                .orElseGet(Collections::emptyMap);
    }
}

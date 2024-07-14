package xyz.brassgoggledcoders.minescribe.mod.event;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod.EventBusSubscriber;
import xyz.brassgoggledcoders.minescribe.mod.MineScribeMod;
import xyz.brassgoggledcoders.minescribe.mod.api.event.AddMineScribeReloadListenerEvent;
import xyz.brassgoggledcoders.minescribe.mod.api.event.AddMineScribeWriteableManagerEvent;
import xyz.brassgoggledcoders.minescribe.mod.api.models.*;
import xyz.brassgoggledcoders.minescribe.mod.resource.CollectingWritingManager;
import xyz.brassgoggledcoders.minescribe.mod.resource.LanguageWriterManager;
import xyz.brassgoggledcoders.minescribe.mod.resource.WriteableCodecManager;
import xyz.brassgoggledcoders.minescribe.mod.utils.MineScribeStringHelper;

import java.util.Collections;
import java.util.Optional;

@EventBusSubscriber(modid = MineScribeMod.MODID)
public class MineScribeCommonHandler {
    private static WriteableCodecManager<MineScribePackType> packTypeManager;
    private static WriteableCodecManager<PackRepository> packRepositoryManager;
    private static WriteableCodecManager<ContentCategory> contentCategoryManager;

    @SubscribeEvent
    public static void addMineScribeLoadListeners(AddMineScribeReloadListenerEvent event) {
        packTypeManager = new WriteableCodecManager<>("pack_type", MineScribePackType.CODEC);
        event.addListener(packTypeManager);

        packRepositoryManager = new WriteableCodecManager<>("pack_repository", PackRepository.CODEC);
        event.addListener(packRepositoryManager);

        contentCategoryManager = new WriteableCodecManager<>("content_category", ContentCategory.CODEC);
        event.addListener(contentCategoryManager);
    }

    @SubscribeEvent
    public static void addMineScribeWriteManager(AddMineScribeWriteableManagerEvent event) {
        event.addWritableManager(packTypeManager);
        event.addWritableManager(packRepositoryManager);
        event.addWritableManager(new LanguageWriterManager());
        event.addWritableManager(contentCategoryManager);

        RegistryAccess registryAccess = event.getServer()
                .registryAccess();
        event.addWritableManager(new CollectingWritingManager<>(
                "content_type",
                () -> registryAccess.registries()
                        .<Pair<ResourceLocation, RegistryAccess.RegistryEntry<?>>>map(registry -> {
                            ResourceLocation registryId = registry.key()
                                    .location();
                            ResourceLocation id = new ResourceLocation(registryId.getNamespace(), "tag/" + registryId.getPath());
                            return Pair.of(id, registry);
                        }),
                registryEntry -> new ContentType(
                        Component.literal(MineScribeStringHelper.toTitleCase(registryEntry.key()) + " Tags"),
                        Optional.of(new ResourceLocation("minecraft", "tags")),
                        new MineScribePath(
                                "NAMESPACE",
                                TagManager.getTagDir(registryEntry.key())
                        ),
                        Collections.singletonList(new ResourceLocation("minecraft", "data"))
                ),
                ContentType.CODEC
        ));
    }
}

package xyz.brassgoggledcoders.minescribe.mod.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod.EventBusSubscriber;
import xyz.brassgoggledcoders.minescribe.mod.MineScribeMod;
import xyz.brassgoggledcoders.minescribe.mod.api.event.AddMineScribeReloadListenerEvent;
import xyz.brassgoggledcoders.minescribe.mod.api.event.AddMineScribeWriteableManagerEvent;
import xyz.brassgoggledcoders.minescribe.mod.api.models.MineScribePackType;
import xyz.brassgoggledcoders.minescribe.mod.api.models.PackRepository;
import xyz.brassgoggledcoders.minescribe.mod.resource.LanguageWriterManager;
import xyz.brassgoggledcoders.minescribe.mod.resource.WriteableCodecManager;

@EventBusSubscriber(modid = MineScribeMod.MODID)
public class MineScribeCommonHandler {
    private static WriteableCodecManager<MineScribePackType> packTypeManager;
    private static WriteableCodecManager<PackRepository> packRepositoryManager;

    @SubscribeEvent
    public static void addMineScribeLoadListeners(AddMineScribeReloadListenerEvent event) {
        packTypeManager = new WriteableCodecManager<>("pack_type", MineScribePackType.CODEC);
        event.addListener(packTypeManager);

        packRepositoryManager = new WriteableCodecManager<>("pack_repository", PackRepository.CODEC);
        event.addListener(packRepositoryManager);
    }

    @SubscribeEvent
    public static void addMineScribeWriteManager(AddMineScribeWriteableManagerEvent event) {
        event.addWritableManager(packTypeManager);
        event.addWritableManager(packRepositoryManager);
        event.addWritableManager(new LanguageWriterManager());
    }
}

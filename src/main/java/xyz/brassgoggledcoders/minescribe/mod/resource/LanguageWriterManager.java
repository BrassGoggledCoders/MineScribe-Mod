package xyz.brassgoggledcoders.minescribe.mod.resource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.locale.Language;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import xyz.brassgoggledcoders.minescribe.mod.MineScribeMod;
import xyz.brassgoggledcoders.minescribe.mod.api.resource.IWriteableManager;
import xyz.brassgoggledcoders.minescribe.mod.api.resource.WriteDetails;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LanguageWriterManager implements IWriteableManager {
    private final static Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    @Override
    public Stream<WriteDetails> getWriteDetails() {
        return Stream.of(new WriteDetails(
                new ResourceLocation(MineScribeMod.MODID, "lang/en_us.json"),
                PackType.CLIENT_RESOURCES,
                () -> GSON.toJson(this.getMineScribeLanguageData())
        ));
    }

    private Map<String, String> getMineScribeLanguageData() {
        return Language.getInstance()
                .getLanguageData()
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey()
                        .contains(MineScribeMod.MODID)
                )
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }
}

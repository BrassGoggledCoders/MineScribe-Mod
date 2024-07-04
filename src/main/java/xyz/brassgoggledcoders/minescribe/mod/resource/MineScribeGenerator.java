package xyz.brassgoggledcoders.minescribe.mod.resource;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.neoforged.fml.loading.FMLPaths;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import xyz.brassgoggledcoders.minescribe.mod.api.resource.IWriteableManager;
import xyz.brassgoggledcoders.minescribe.mod.api.resource.WriteDetails;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MineScribeGenerator {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Path MINESCRIBE_PATH = FMLPaths.GAMEDIR.get()
            .resolve(".minescribe");
    private static final Path RESOURCES_PATH = MINESCRIBE_PATH.resolve("builtin");
    private static final Map<PackType, Path> PACK_PATHS = new HashMap<>();


    public static FileCounter generate(List<IWriteableManager> writeableManagerList) {
        try {
            FileUtils.deleteDirectory(RESOURCES_PATH.toFile());
        } catch (IOException e) {
            LOGGER.error("Failed to delete existing files", e);
        }
        FileCounter finalFileCounter = writeableManagerList.parallelStream()
                .flatMap(IWriteableManager::getWriteDetails)
                .collect(
                        FileCounter::new,
                        (fileCounter, writeDetails) -> {
                            if (MineScribeGenerator.writeDetails(writeDetails)) {
                                fileCounter.increaseSuccess();
                            } else {
                                fileCounter.increaseFailure();
                            }
                        },
                        FileCounter::combine
                );

        try {
            Files.writeString(MINESCRIBE_PATH.resolve(".load-complete"), "");
        } catch (IOException e) {
            LOGGER.error("Failed to write load-complete file", e);
        }

        return finalFileCounter;
    }

    private static boolean writeDetails(WriteDetails writeDetails) {
        ResourceLocation writeLocation = writeDetails.writeLocation();
        Path writePath = PACK_PATHS.computeIfAbsent(
                        writeDetails.packType(),
                        packType -> RESOURCES_PATH.resolve(packType.getDirectory())
                )
                .resolve(writeLocation.getNamespace())
                .resolve(writeLocation.getPath());

        try {
            Files.createDirectories(writePath.getParent());
            Files.writeString(
                    writePath,
                    writeDetails.contents()
                            .get(),
                    StandardOpenOption.CREATE_NEW,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
            return true;
        } catch (IOException e) {
            LOGGER.error("Failed to write file: {}", writeLocation, e);
            return false;
        }
    }
}

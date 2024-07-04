package xyz.brassgoggledcoders.minescribe.mod.command;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.logging.LogUtils;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.resources.CloseableResourceManager;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.slf4j.Logger;
import xyz.brassgoggledcoders.minescribe.mod.MineScribeMod;
import xyz.brassgoggledcoders.minescribe.mod.api.MineScribeAPI;
import xyz.brassgoggledcoders.minescribe.mod.api.event.AddMineScribeWriteableManagerEvent;
import xyz.brassgoggledcoders.minescribe.mod.api.resource.ReloadableMineScribeResources;
import xyz.brassgoggledcoders.minescribe.mod.resource.MineScribeGenerator;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = MineScribeMod.MODID)
public class MineScribeCommand {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        event.getDispatcher()
                .register(createCommand());
    }

    public static LiteralArgumentBuilder<CommandSourceStack> createCommand() {
        return Commands.literal(MineScribeMod.MODID)
                .then(Commands.literal("generate")
                        .requires(cs -> cs.hasPermission(2))
                        .executes(commandContext -> {
                            MinecraftServer server = commandContext.getSource()
                                    .getServer();

                            reloadResources(server, commandContext.getSource());
                            return 1;
                        })
                );
    }

    private static void reloadResources(MinecraftServer server, CommandSourceStack source) {
        Collection<String> selectedIds = server.getPackRepository()
                .getSelectedIds();
        RegistryAccess.Frozen frozen = server.registryAccess();
        CompletableFuture.supplyAsync(
                        () -> selectedIds.stream()
                                .map(server.getPackRepository()::getPack)
                                .filter(Objects::nonNull)
                                .flatMap(Pack::streamSelfAndChildren)
                                .map(Pack::open)
                                .collect(ImmutableList.toImmutableList()),
                        server
                )
                .thenCompose(packResources -> {
                    CloseableResourceManager packResourceManager = new MultiPackResourceManager(
                            MineScribeAPI.MINESCRIBE_PACK_TYPE,
                            packResources
                    );
                    return ReloadableMineScribeResources.loadResources(
                                    server,
                                    packResourceManager,
                                    frozen,
                                    Util.backgroundExecutor(),
                                    server
                            )
                            .whenComplete((serverResources, throwable) -> {
                                if (throwable != null) {
                                    packResourceManager.close();
                                }
                            });
                })
                .thenApplyAsync(
                        reloadableResources -> NeoForge.EVENT_BUS.post(new AddMineScribeWriteableManagerEvent())
                                .getWriteableManagers(),
                        server
                )
                .thenApplyAsync(
                        MineScribeGenerator::generate,
                        Util.backgroundExecutor()
                )
                .whenCompleteAsync(
                        (fileCounter, throwable) -> {
                            if (throwable != null) {
                                LOGGER.error("Failed to generate", throwable);
                                source.sendFailure(Component.literal("Failed to generate. Check Logs for errors"));
                            } else {
                                source.sendSuccess(
                                        () -> Component.literal("Finished Generation. Total %s, Success %s, Failure %s"
                                                .formatted(fileCounter.total(), fileCounter.success(), fileCounter.failure())
                                        ),
                                        true
                                );
                            }
                        },
                        server
                );
    }
}

package xyz.brassgoggledcoders.minescribe.mod.api.resource;

import com.mojang.logging.LogUtils;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleReloadInstance;
import net.minecraft.util.Unit;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.slf4j.Logger;
import xyz.brassgoggledcoders.minescribe.mod.api.event.AddMineScribeReloadListenerEvent;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public record ReloadableMineScribeResources(
        ICondition.IContext conditionContext
) {
    private static final CompletableFuture<Unit> INITIAL_TASK = CompletableFuture.completedFuture(Unit.INSTANCE);
    private final static Logger LOGGER = LogUtils.getLogger();


    public static CompletableFuture<ReloadableMineScribeResources> loadResources(
            MinecraftServer server,
            ResourceManager resourceManager,
            RegistryAccess.Frozen registryAccess,
            Executor backgroundExecutor,
            Executor serverExecutor
    ) {
        ReloadableMineScribeResources reloadableResources = new ReloadableMineScribeResources(new MineScribeConditionContext(server));
        List<PreparableReloadListener> listeners = collectListeners(reloadableResources, registryAccess);
        return SimpleReloadInstance.create(
                        resourceManager,
                        listeners,
                        backgroundExecutor,
                        serverExecutor,
                        INITIAL_TASK,
                        LOGGER.isDebugEnabled()
                )
                .done()
                .thenApply(p_214306_ -> reloadableResources);
    }

    public static List<PreparableReloadListener> collectListeners(ReloadableMineScribeResources resources, RegistryAccess registryAccess) {
        return NeoForge.EVENT_BUS.post(new AddMineScribeReloadListenerEvent(resources, registryAccess))
                .getListeners();
    }
}

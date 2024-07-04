package xyz.brassgoggledcoders.minescribe.mod.api.event;

import net.minecraft.core.RegistryAccess;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.conditions.ICondition;
import xyz.brassgoggledcoders.minescribe.mod.api.resource.ReloadableMineScribeResources;

import java.util.ArrayList;
import java.util.List;

public class AddMineScribeReloadListenerEvent extends Event {
    private final ReloadableMineScribeResources mineScribeResources;
    private final RegistryAccess registryAccess;

    private final List<PreparableReloadListener> listeners;

    public AddMineScribeReloadListenerEvent(ReloadableMineScribeResources mineScribeResources, RegistryAccess registryAccess) {
        this.mineScribeResources = mineScribeResources;
        this.registryAccess = registryAccess;

        this.listeners = new ArrayList<>();
    }

    public void addListener(PreparableReloadListener listener) {
        this.listeners.add(listener);
    }

    public List<PreparableReloadListener> getListeners() {
        return this.listeners;
    }

    public ReloadableMineScribeResources getMineScribeResources() {
        return mineScribeResources;
    }

    public RegistryAccess getRegistryAccess() {
        return registryAccess;
    }

    public ICondition.IContext getConditionContext() {
        return this.mineScribeResources.conditionContext();
    }
}

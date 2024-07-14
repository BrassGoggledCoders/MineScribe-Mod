package xyz.brassgoggledcoders.minescribe.mod.api.event;

import net.minecraft.core.RegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.Event;
import xyz.brassgoggledcoders.minescribe.mod.api.resource.IWriteableManager;

import java.util.ArrayList;
import java.util.List;

public class AddMineScribeWriteableManagerEvent extends Event {
    private final List<IWriteableManager> writeableManagers;
    private final MinecraftServer server;

    public AddMineScribeWriteableManagerEvent(MinecraftServer minecraftServer) {
        this.server = minecraftServer;
        this.writeableManagers = new ArrayList<>();
    }

    public MinecraftServer getServer() {
        return server;
    }

    public void addWritableManager(IWriteableManager writeableManager) {
        this.writeableManagers.add(writeableManager);
    }

    public List<IWriteableManager> getWriteableManagers() {
        return this.writeableManagers;
    }
}

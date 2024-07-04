package xyz.brassgoggledcoders.minescribe.mod.api.event;

import net.neoforged.bus.api.Event;
import xyz.brassgoggledcoders.minescribe.mod.api.resource.IWriteableManager;

import java.util.ArrayList;
import java.util.List;

public class AddMineScribeWriteableManagerEvent extends Event {
    private final List<IWriteableManager> writeableManagers;

    public AddMineScribeWriteableManagerEvent() {

        this.writeableManagers = new ArrayList<>();
    }

    public void addWritableManager(IWriteableManager writeableManager) {
        this.writeableManagers.add(writeableManager);
    }

    public List<IWriteableManager> getWriteableManagers() {
        return this.writeableManagers;
    }
}

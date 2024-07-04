package xyz.brassgoggledcoders.minescribe.mod.api.resource;

import java.util.stream.Stream;

public interface IWriteableManager {

    Stream<WriteDetails> getWriteDetails();
}

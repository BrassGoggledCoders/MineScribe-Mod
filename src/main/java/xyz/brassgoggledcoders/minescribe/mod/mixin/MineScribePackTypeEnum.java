package xyz.brassgoggledcoders.minescribe.mod.mixin;

import net.minecraft.server.packs.PackType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.brassgoggledcoders.minescribe.mod.api.MineScribeAPI;

import java.util.Arrays;

@Mixin(PackType.class)
public class MineScribePackTypeEnum {
    @Shadow
    @Final
    @Mutable
    private static PackType[] $VALUES;

    @Invoker(value="<init>")
    @SuppressWarnings("SameParameterValue")
    private static PackType create(String name, int ordinal, String directoryName)
    {
        throw new IllegalStateException("Invoker Failed");
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void classInit(CallbackInfo cb)
    {
        var entry = create("MINESCRIBE", $VALUES.length, "minescribe");

        MineScribeAPI.MINESCRIBE_PACK_TYPE = entry;

        $VALUES = Arrays.copyOf($VALUES, $VALUES.length + 1);
        $VALUES[$VALUES.length-1] = entry;
    }
}

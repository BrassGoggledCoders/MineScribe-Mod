package xyz.brassgoggledcoders.minescribe.mod.utils;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class MineScribeStringHelper {
    public static <T> String toTitleCase(ResourceKey<T> resourceKey) {
        return toTitleCase(resourceKey.location());
    }

    public static String toTitleCase(ResourceLocation resourceLocation) {
        return toTitleCase(resourceLocation.getPath()
                .replace("_", " ")
                .replace("/", " ")
        );
    }

    public static String toTitleCase(String phrase) {

        // convert the string to an array
        char[] phraseChars = phrase.toCharArray();
        if (phraseChars.length > 0) {
            phraseChars[0] = Character.toTitleCase(phraseChars[0]);
        }

        for (int i = 0; i < phraseChars.length - 1; i++) {
            if (Character.isWhitespace(phraseChars[i])) {
                phraseChars[i + 1] = Character.toUpperCase(phraseChars[i + 1]);
            }
        }

        // convert the array to string
        return String.valueOf(phraseChars);
    }
}

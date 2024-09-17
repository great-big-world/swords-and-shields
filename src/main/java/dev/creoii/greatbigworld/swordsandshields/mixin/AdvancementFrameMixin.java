package dev.creoii.greatbigworld.swordsandshields.mixin;

import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(AdvancementFrame.class)
public class AdvancementFrameMixin {
    @Invoker("<init>")
    private static AdvancementFrame init(String name, int id, final String stringId, final Formatting titleFormat) {
        throw new AssertionError();
    }

    @Shadow
    @Final
    @Mutable
    private static AdvancementFrame[] field_1253;

    static {
        ArrayList<AdvancementFrame> values = new ArrayList<>(Arrays.asList(field_1253));
        int last = values.size();

        System.out.println(field_1253.length);
        values.add(init("ENCHANTMENT", last, "enchantment", Formatting.LIGHT_PURPLE));

        field_1253 = values.toArray(new AdvancementFrame[0]);
        System.out.println(field_1253.length);
    }
}
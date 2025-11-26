package dev.creoii.greatbigworld.swordsandshields.mixin;

import dev.creoii.greatbigworld.knowledge.Knowledge;
import dev.creoii.greatbigworld.knowledge.KnowledgeManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(EnchantmentScreenHandler.class)
public class EnchantmentScreenHandlerMixin extends ScreenHandlerMixin {
    @Shadow
    @Final
    private Random random;

    @Inject(method = "generateEnchantments", at = @At(value = "INVOKE", target = "Lnet/minecraft/registry/Registry;getOptional(Lnet/minecraft/registry/tag/TagKey;)Ljava/util/Optional;"), cancellable = true)
    private void gbw$generateEnchantmentsFromKnowledge(DynamicRegistryManager registryManager, ItemStack stack, int slot, int level, CallbackInfoReturnable<List<EnchantmentLevelEntry>> cir) {
        if (gbw$getPlayer() != null && !gbw$getPlayer().getEntityWorld().isClient()) {
            Registry<Enchantment> registry = registryManager.getOrThrow(RegistryKeys.ENCHANTMENT);
            KnowledgeManager knowledgeManager = KnowledgeManager.getServerState(gbw$getPlayer().getEntityWorld().getServer());
            List<RegistryEntry<Enchantment>> registryEntries = new ArrayList<>();
            Set<Knowledge> knowledges = knowledgeManager.getPlayerKnowledge(gbw$getPlayer(), Knowledge.Type.ENCHANTMENT);

            if (knowledges == null || knowledges.isEmpty()) {
                cir.setReturnValue(List.of());
                return;
            }

            knowledges.forEach(knowledge -> {
                Optional<RegistryEntry.Reference<Enchantment>> optional = registry.getEntry(knowledge.data());
                optional.ifPresent(enchantmentReference -> {
                    if (enchantmentReference.isIn(EnchantmentTags.IN_ENCHANTING_TABLE))
                        registryEntries.add(enchantmentReference);
                });
            });

            if (registryEntries.isEmpty()) {
                cir.setReturnValue(List.of());
                return;
            }

            RegistryEntryList<Enchantment> entries = RegistryEntryList.of(registryEntries);
            List<EnchantmentLevelEntry> list = EnchantmentHelper.generateEnchantments(random, stack, level, entries.stream());
            if (stack.isOf(Items.BOOK) && list.size() > 1) {
                list.remove(random.nextInt(list.size()));
            }
            cir.setReturnValue(list);
            return;
        }
        cir.setReturnValue(List.of());
    }
}

package dev.creoii.greatbigworld.swordsandshields.client;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.creoii.greatbigworld.swordsandshields.util.ElementHolder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

@Environment(EnvType.CLIENT)
public record EnchantmentElementProperty() implements SelectItemModelProperty<String> {
    public static final SelectItemModelProperty.Type<EnchantmentElementProperty, String> TYPE = Type.create(MapCodec.unit(new EnchantmentElementProperty()), Codec.STRING);

    @Override
    public Type<? extends SelectItemModelProperty<String>, String> type() {
        return TYPE;
    }

    @Override
    public String get(ItemStack stack, @Nullable ClientLevel world, @Nullable LivingEntity entity, int seed, ItemDisplayContext displayContext) {
        ItemEnchantments itemEnchantments = stack.get(DataComponents.STORED_ENCHANTMENTS);

        if (itemEnchantments != null) {
            Holder<Enchantment> holder = itemEnchantments.keySet().iterator().next();
            if (holder.unwrapKey().isPresent()) {
                return ElementHolder.gbw$getElement(holder.unwrapKey().get()).name().toLowerCase(Locale.ROOT);
            }
        }
        
        return "";
    }

    @Override
    public Codec<String> valueCodec() {
        return Codec.STRING;
    }
}

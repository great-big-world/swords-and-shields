package dev.creoii.greatbigworld.swordsandshields;

import com.google.common.collect.ImmutableList;
import dev.creoii.greatbigworld.mixin.AbstractBlockStateAccessor;
import dev.creoii.greatbigworld.swordsandshields.item.SwordsAndShieldsArmorMaterials;
import dev.creoii.greatbigworld.swordsandshields.item.SwordsAndShieldsToolMaterials;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;

import java.util.List;

public class SwordsAndShields implements ModInitializer {
    private static final List<Item> MEATS = new ImmutableList.Builder<Item>()
            .add(Items.BEEF).add(Items.COOKED_BEEF)
            .add(Items.PORKCHOP).add(Items.COOKED_PORKCHOP)
            .add(Items.CHICKEN).add(Items.COOKED_CHICKEN)
            .add(Items.MUTTON).add(Items.COOKED_MUTTON)
            .add(Items.COD).add(Items.COOKED_COD)
            .add(Items.SALMON).add(Items.COOKED_SALMON)
            .build();
    private static final List<Item> PIES = new ImmutableList.Builder<Item>()
            .add(Items.PUMPKIN_PIE)
            .build();

    @Override
    public void onInitialize() {
        SwordsAndShieldsArmorMaterials.register();
        SwordsAndShieldsToolMaterials.register();

        ((AbstractBlockStateAccessor) Blocks.ENCHANTING_TABLE.getDefaultState()).setHardness(15f);
        ((AbstractBlockStateAccessor) Blocks.SPAWNER.getDefaultState()).setLuminance(4);

        DefaultItemComponentEvents.MODIFY.register(modifyContext -> {
            modifyContext.modify(item -> item instanceof PotionItem, (builder, item) -> {
                builder.add(DataComponentTypes.MAX_STACK_SIZE, 4);
            });
            modifyContext.modify(MEATS::contains, (builder, item) -> {
                builder.add(DataComponentTypes.MAX_STACK_SIZE, 16);
            });
            modifyContext.modify(PIES::contains, (builder, item) -> {
                builder.add(DataComponentTypes.MAX_STACK_SIZE, 1);
            });
        });
    }
}

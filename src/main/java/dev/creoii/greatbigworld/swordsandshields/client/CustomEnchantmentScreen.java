package dev.creoii.greatbigworld.swordsandshields.client;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.cursor.CursorTypes;
import dev.creoii.greatbigworld.element.Element;
import dev.creoii.greatbigworld.swordsandshields.menu.CustomEnchantmentMenu;
import dev.creoii.greatbigworld.swordsandshields.util.ElementHolder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.EnchantmentNames;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.book.BookModel;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.*;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class CustomEnchantmentScreen extends AbstractContainerScreen<CustomEnchantmentMenu> {
    private static final Identifier[] ENABLED_LEVEL_SPRITES = new Identifier[]{Identifier.withDefaultNamespace("container/enchanting_table/level_1"), Identifier.withDefaultNamespace("container/enchanting_table/level_2"), Identifier.withDefaultNamespace("container/enchanting_table/level_3")};
    private static final Identifier[] DISABLED_LEVEL_SPRITES = new Identifier[]{Identifier.withDefaultNamespace("container/enchanting_table/level_1_disabled"), Identifier.withDefaultNamespace("container/enchanting_table/level_2_disabled"), Identifier.withDefaultNamespace("container/enchanting_table/level_3_disabled")};
    private static final Identifier ENCHANTMENT_SLOT_DISABLED_SPRITE = Identifier.withDefaultNamespace("container/enchanting_table/enchantment_slot_disabled");
    private static final Identifier ENCHANTMENT_SLOT_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("container/enchanting_table/enchantment_slot_highlighted");
    private static final Identifier ENCHANTMENT_SLOT_SPRITE = Identifier.withDefaultNamespace("container/enchanting_table/enchantment_slot");
    private static final Identifier ENCHANTING_TABLE_LOCATION = Identifier.withDefaultNamespace("textures/gui/container/enchanting_table.png");
    private static final Identifier ENCHANTING_BOOK_LOCATION = Identifier.withDefaultNamespace("textures/entity/enchanting_table_book.png");
    private static final Component SPACE = Component.literal(" ");
    private final RandomSource random = RandomSource.create();
    private BookModel bookModel;
    public float flip;
    public float oFlip;
    public float flipT;
    public float flipA;
    public float open;
    public float oOpen;
    private ItemStack last;

    public CustomEnchantmentScreen(CustomEnchantmentMenu enchantmentMenu, Inventory inventory, Component component) {
        super(enchantmentMenu, inventory, component);
        last = ItemStack.EMPTY;
    }

    protected void init() {
        super.init();
        bookModel = new BookModel(minecraft.getEntityModels().bakeLayer(ModelLayers.BOOK));
    }

    public void containerTick() {
        super.containerTick();
        minecraft.player.experienceDisplayStartTick = minecraft.player.tickCount;
        tickBook();
    }

    public boolean mouseClicked(MouseButtonEvent mouseButtonEvent, boolean bl) {
        int i = (width - imageWidth) / 2;
        int j = (height - imageHeight) / 2;

        for(int k = 0; k < 3; ++k) {
            double d = mouseButtonEvent.x() - (double)(i + 60);
            double e = mouseButtonEvent.y() - (double)(j + 14 + 19 * k);
            if (d >= (double)0.0F && e >= (double)0.0F && d < (double)108.0F && e < (double)19.0F && menu.clickMenuButton(minecraft.player, k)) {
                minecraft.gameMode.handleInventoryButtonClick(menu.containerId, k);
                return true;
            }
        }

        return super.mouseClicked(mouseButtonEvent, bl);
    }

    protected void renderBg(GuiGraphics guiGraphics, float f, int i, int j) {
        int k = (width - imageWidth) / 2;
        int l = (height - imageHeight) / 2;
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, ENCHANTING_TABLE_LOCATION, k, l, 0.0F, 0.0F, imageWidth, imageHeight, 256, 256);
        renderBook(guiGraphics, k, l);
        EnchantmentNames.getInstance().initSeed(menu.getEnchantmentSeed());
        int m = menu.getGoldCount();

        for(int n = 0; n < 3; ++n) {
            int o = k + 60;
            int p = o + 20;
            int q = menu.costs[n];
            if (q == 0) {
                guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, ENCHANTMENT_SLOT_DISABLED_SPRITE, o, l + 14 + 19 * n, 108, 19);
            } else {
                String string = "" + q;
                int r = 86 - font.width(string);
                FormattedText formattedText = EnchantmentNames.getInstance().getRandomName(font, r);
                int s = -9937334;
                if ((m < n + 1 || minecraft.player.experienceLevel < q) && !minecraft.player.hasInfiniteMaterials()) {
                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, ENCHANTMENT_SLOT_DISABLED_SPRITE, o, l + 14 + 19 * n, 108, 19);
                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, DISABLED_LEVEL_SPRITES[n], o + 1, l + 15 + 19 * n, 16, 16);
                    guiGraphics.drawWordWrap(font, formattedText, p, l + 16 + 19 * n, r, ARGB.opaque((s & 16711422) >> 1), false);
                    s = -12550384;
                } else {
                    int t = i - (k + 60);
                    int u = j - (l + 14 + 19 * n);
                    if (t >= 0 && u >= 0 && t < 108 && u < 19) {
                        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, ENCHANTMENT_SLOT_HIGHLIGHTED_SPRITE, o, l + 14 + 19 * n, 108, 19);
                        guiGraphics.requestCursor(CursorTypes.POINTING_HAND);
                        s = -128;
                    } else {
                        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, ENCHANTMENT_SLOT_SPRITE, o, l + 14 + 19 * n, 108, 19);
                    }

                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, ENABLED_LEVEL_SPRITES[n], o + 1, l + 15 + 19 * n, 16, 16);
                    guiGraphics.drawWordWrap(font, formattedText, p, l + 16 + 19 * n, r, s, false);
                    s = -8323296;
                }

                guiGraphics.drawString(font, string, p + 86 - font.width(string), l + 16 + 19 * n + 7, s);
            }
        }
    }

    private void renderBook(GuiGraphics guiGraphics, int i, int j) {
        float f = minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(false);
        float g = Mth.lerp(f, oOpen, open);
        float h = Mth.lerp(f, oFlip, flip);
        int k = i + 14;
        int l = j + 14;
        int m = k + 38;
        int n = l + 31;
        guiGraphics.submitBookModelRenderState(bookModel, ENCHANTING_BOOK_LOCATION, 40.0F, g, h, k, l, m, n);
    }

    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        float g = minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(false);
        super.render(guiGraphics, i, j, g);
        renderTooltip(guiGraphics, i, j);
        boolean bl = minecraft.player.hasInfiniteMaterials();

        int k = menu.getGoldCount();

        for(int l = 0; l < 3; ++l) {
            int m = menu.costs[l];
            Optional<Holder.Reference<Enchantment>> optional = this.minecraft.level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(this.menu.enchantClue[l]);
            if (optional.isEmpty())
                continue;

            int n = menu.levelClue[l];
            int o = l + 1;
            if (!isHovering(60, 14 + 19 * l, 108, 17, i, j) || m <= 0 || n < 0)
                continue;

            List<Component> list = Lists.newArrayList();

            Element element = ElementHolder.gbw$getElement(optional.get().key());
            Component name = element == null ? Enchantment.getFullname(optional.get(), n) : ComponentUtils.formatList(List.of(element.getSymbol(), Enchantment.getFullname(optional.get(), n)), SPACE);

            list.add(Component.translatable("container.enchant.clue", name).withStyle(ChatFormatting.WHITE));
            if (!bl) {
                list.add(CommonComponents.EMPTY);
                if (minecraft.player.experienceLevel < m) {
                    list.add(Component.translatable("container.enchant.level.requirement", menu.costs[l]).withStyle(ChatFormatting.RED));
                } else {
                    MutableComponent mutableComponent = o == 1 ? Component.translatable("container.enchant.lapis.one") : Component.translatable("container.enchant.lapis.many", o);
                    list.add(mutableComponent.withStyle(k >= o ? ChatFormatting.GRAY : ChatFormatting.RED));

                    MutableComponent mutableComponent2 = o == 1 ? Component.translatable("container.enchant.level.one") : Component.translatable("container.enchant.level.many", o);
                    list.add(mutableComponent2.withStyle(ChatFormatting.GRAY));
                }
            }

            guiGraphics.setComponentTooltipForNextFrame(font, list, i, j);
            break;
        }

        System.out.println("enchants: " + Arrays.toString(menu.enchantClue));
        System.out.println("levels: " + Arrays.toString(menu.levelClue));
    }

    public void tickBook() {
        ItemStack itemStack = menu.getSlot(0).getItem();
        if (!ItemStack.matches(itemStack, last)) {
            last = itemStack;

            do {
                flipT += (float)(random.nextInt(4) - random.nextInt(4));
            } while(flip <= flipT + 1.0F && flip >= flipT - 1.0F);
        }

        oFlip = flip;
        oOpen = open;
        boolean bl = false;

        for(int i = 0; i < 3; ++i) {
            if (menu.costs[i] != 0) {
                bl = true;
                break;
            }
        }

        if (bl) {
            open += 0.2F;
        } else {
            open -= 0.2F;
        }

        open = Mth.clamp(open, 0.0F, 1.0F);
        float f = (flipT - flip) * 0.4F;
        f = Mth.clamp(f, -0.2F, 0.2F);
        flipA += (f - flipA) * 0.9F;
        flip += flipA;
    }
}

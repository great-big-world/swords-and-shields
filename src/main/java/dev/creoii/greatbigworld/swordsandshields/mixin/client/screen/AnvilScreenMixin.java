package dev.creoii.greatbigworld.swordsandshields.mixin.client.screen;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.cursor.CursorTypes;
import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.knowledge.KnowledgeUtil;
import dev.creoii.greatbigworld.swordsandshields.util.ExtendedAnvilMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(AnvilScreen.class)
public abstract class AnvilScreenMixin extends ItemCombinerScreen<AnvilMenu> {
    @Shadow @Final private static Identifier ERROR_SPRITE;

    @Unique private static final Identifier SCROLLER_SPRITE = Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "container/anvil/scroller");
    @Unique private static final Identifier SCROLLER_DISABLED_SPRITE = Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "container/anvil/scroller_disabled");
    @Unique private static final Identifier RECIPE_SELECTED_SPRITE = Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "container/anvil/recipe_selected");
    @Unique private static final Identifier RECIPE_HIGHLIGHTED_SPRITE = Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "container/anvil/recipe_highlighted");
    @Unique private static final Identifier RECIPE_SPRITE = Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "container/anvil/recipe");
    @Unique private float scrollOffs;
    @Unique private boolean scrolling;
    @Unique private int startIndex;
    @Unique private boolean displayTrims;

    public AnvilScreenMixin(AnvilMenu itemCombinerMenu, Inventory inventory, Component component, Identifier identifier) {
        super(itemCombinerMenu, inventory, component, identifier);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void gbw$offsetInventoryLabelY(AnvilMenu anvilMenu, Inventory inventory, Component component, CallbackInfo ci) {
        inventoryLabelY += 31;
        imageHeight = 197;
        ((ExtendedAnvilMenu) anvilMenu).gbw$registerUpdateListener(this::containerChanged);
    }

    @Inject(method = "renderBg", at = @At("TAIL"))
    private void gbw$renderAnvilBg(GuiGraphics guiGraphics, float f, int i, int j, CallbackInfo ci) {
        int n = leftPos + 151;
        int o = topPos + 46 + (int)(41f * scrollOffs);
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, isScrollBarActive() ? SCROLLER_SPRITE : SCROLLER_DISABLED_SPRITE, n, o, 12, 15);
        if (i >= n && i < n + 12 && j >= o && j < o + 15) {
            guiGraphics.requestCursor(scrolling ? CursorTypes.RESIZE_NS : CursorTypes.POINTING_HAND);
        }

        int p = leftPos + 84;
        int q = topPos + 45;
        int r = startIndex + 12;
        renderButtons(guiGraphics, i, j, p, q, r);
        renderTrimPatterns(guiGraphics, p, q, r);
    }

    protected void renderTooltip(GuiGraphics guiGraphics, int i, int j) {
        super.renderTooltip(guiGraphics, i, j);
        if (displayTrims) {
            int k = leftPos + 84;
            int l = topPos + 45;
            int m = startIndex + 12;
            Set<TrimPattern> patterns = ((ExtendedAnvilMenu) menu).gbw$getKnownPatterns();
            Registry<TrimPattern> registry = minecraft.level.registryAccess().lookupOrThrow(Registries.TRIM_PATTERN);

            TrimPattern[] trimPatterns = patterns.toArray(TrimPattern[]::new);
            for(int n = startIndex; n < m && n < patterns.size(); ++n) {
                int o = n - startIndex;
                int p = k + o % 4 * 16;
                int q = l + o / 4 * 18 + 2;
                if (i >= p && i < p + 16 && j >= q && j < q + 18) {
                    Item item = KnowledgeUtil.getStackFromArmorTrimPattern(registry.getResourceKey(trimPatterns[n]).get());
                    if (item == null)
                        continue;
                    guiGraphics.setTooltipForNextFrame(font, item.getDefaultInstance(), i, j);
                }
            }
        }
    }

    @Unique
    private void renderButtons(GuiGraphics guiGraphics, int i, int j, int k, int l, int m) {
        for (int n = startIndex; n < m && n < ((ExtendedAnvilMenu) menu).gbw$getNumberOfVisiblePatterns(); ++n) {
            int o = n - startIndex;
            int p = k + o % 4 * 16;
            int q = o / 4;
            int r = l + q * 18 + 2;
            Identifier identifier;
            if (n == ((ExtendedAnvilMenu) menu).gbw$getSelectedRecipeIndex()) {
                identifier = RECIPE_SELECTED_SPRITE;
            } else if (i >= p && j >= r && i < p + 16 && j < r + 18) {
                identifier = RECIPE_HIGHLIGHTED_SPRITE;
            } else {
                identifier = RECIPE_SPRITE;
            }

            int s = r - 1;
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, identifier, p, s, 16, 18);
            if (i >= p && j >= s && i < p + 16 && j < s + 18) {
                guiGraphics.requestCursor(CursorTypes.POINTING_HAND);
            }
        }
    }

    @Unique
    private void renderTrimPatterns(GuiGraphics guiGraphics, int i, int j, int k) {
        Set<TrimPattern> patterns = ((ExtendedAnvilMenu) menu).gbw$getKnownPatterns();
        ContextMap contextMap = SlotDisplayContext.fromLevel(minecraft.level);
        Registry<TrimPattern> registry = minecraft.level.registryAccess().lookupOrThrow(Registries.TRIM_PATTERN);

        TrimPattern[] trimPatterns = patterns.toArray(TrimPattern[]::new);
        for (int l = startIndex; l < k && l < patterns.size(); ++l) {
            Item item = KnowledgeUtil.getStackFromArmorTrimPattern(registry.getResourceKey(trimPatterns[l]).get());
            if (item == null)
                continue;

            int m = l - startIndex;
            int n = i + m % 4 * 16;
            int o = m / 4;
            int p = j + o * 18 + 2;
            SlotDisplay slotDisplay = new SlotDisplay.ItemStackSlotDisplay(item.getDefaultInstance());
            guiGraphics.renderItem(slotDisplay.resolveForFirstStack(contextMap), n, p);
        }
    }

    @Inject(method = "renderLabels", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/AnvilMenu;getCost()I"), cancellable = true)
    private void gbw$dontRenderEnchantmentCost(GuiGraphics guiGraphics, int i, int j, CallbackInfo ci) {
        ci.cancel();
    }

    @WrapOperation(method = "renderErrorIcon", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V"))
    private void gbw$adjustErrorIcon(GuiGraphics instance, RenderPipeline renderPipeline, Identifier identifier, int i, int j, int k, int l, Operation<Void> original) {
        instance.blitSprite(RenderPipelines.GUI_TEXTURED, ERROR_SPRITE, i - 63, j + 19, 15, 16);
    }

    public boolean mouseClicked(MouseButtonEvent mouseButtonEvent, boolean bl) {
        if (displayTrims) {
            int i = leftPos + 84;
            int j = topPos + 45;
            int k = startIndex + 12;

            for (int l = startIndex; l < k; ++l) {
                int m = l - startIndex;
                double d = mouseButtonEvent.x() - (double)(i + m % 4 * 16);
                double e = mouseButtonEvent.y() - (double)(j + m / 4 * 18);
                if (d >= (double) 0f && e >= (double) 0f && d < (double) 16f && e < (double) 18f && (menu).clickMenuButton(minecraft.player, l)) {
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1f));
                    minecraft.gameMode.handleInventoryButtonClick(menu.containerId, l);
                    return true;
                }
            }

            i = leftPos + 119;
            j = topPos + 9;
            if (mouseButtonEvent.x() >= (double)i && mouseButtonEvent.x() < (double)(i + 12) && mouseButtonEvent.y() >= (double)j && mouseButtonEvent.y() < (double)(j + 54)) {
                scrolling = true;
            }
        }

        return super.mouseClicked(mouseButtonEvent, bl);
    }

    public boolean mouseDragged(MouseButtonEvent mouseButtonEvent, double d, double e) {
        if (scrolling && isScrollBarActive()) {
            int i = topPos + 14;
            scrollOffs = ((float) mouseButtonEvent.y() - (float) i - 7.5f) / ((float) ((i + 54) - i) - 15f);
            scrollOffs = Mth.clamp(scrollOffs, 0f, 1f);
            startIndex = (int) ((double) (scrollOffs * (float) getOffscreenRows()) + (double) .5f) * 4;
            return true;
        } else return super.mouseDragged(mouseButtonEvent, d, e);
    }

    public boolean mouseReleased(MouseButtonEvent mouseButtonEvent) {
        scrolling = false;
        return super.mouseReleased(mouseButtonEvent);
    }

    public boolean mouseScrolled(double d, double e, double f, double g) {
        if (!super.mouseScrolled(d, e, f, g)) {
            if (isScrollBarActive()) {
                int i = getOffscreenRows();
                float h = (float) g / (float) i;
                scrollOffs = Mth.clamp(scrollOffs - h, 0f, 1f);
                startIndex = (int) ((double) (scrollOffs * (float) i) + (double) .5f) * 4;
            }
        }
        return true;
    }

    @Unique
    public void containerChanged() {
        displayTrims = ((ExtendedAnvilMenu) menu).gbw$hasInputItem();
        if (!displayTrims) {
            scrollOffs = 0f;
            startIndex = 0;
        }
    }

    @Unique
    private boolean isScrollBarActive() {
        return displayTrims && ((ExtendedAnvilMenu) menu).gbw$getNumberOfVisiblePatterns() > 12;
    }

    @Unique
    protected int getOffscreenRows() {
        return (((ExtendedAnvilMenu) menu).gbw$getNumberOfVisiblePatterns() + 4 - 1) / 4 - 3;
    }
}
